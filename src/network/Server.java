package network;

import java.util.UUID;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.concurrent.GlobalEventExecutor;
import model.gamestate.GameMode;
import model.gamestate.Menu;
import model.gamestate.Playing;
import model.player.PlayerMode;
import utils.Vector;

public class Server {

	private static final int port = 4551;
	private ChannelGroup allChannels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE); // Groupe pour tous les
																								// canaux

	public Server() {
		new Thread(() -> {
			try {
				start();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}).start();
	}

	public void start() throws InterruptedException {
		EventLoopGroup bossGroup = new NioEventLoopGroup(); // Gère les connexions
		EventLoopGroup workerGroup = new NioEventLoopGroup(); // Gère les événements

		try {
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch) {
							ch.pipeline().addLast(new StringDecoder()); // Permet de décoder les messages reçus en
																		// String
							ch.pipeline().addLast(new StringEncoder()); // Permet d'encoder les messages avant de les
																		// envoyer
							ch.pipeline().addLast(new ClientHandler()); // Ajout du gestionnaire
						}
					}).option(ChannelOption.SO_BACKLOG, 128) // File d'attente
					.childOption(ChannelOption.SO_KEEPALIVE, true).childOption(ChannelOption.TCP_NODELAY, true);
			System.out.println("Serveur Netty démarré sur le port : " + port);
			bootstrap.bind(port).sync().channel().closeFuture().sync();
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}

	public static String getHeader(String req) {
		return req.split(";")[0];
	}

	public static String[] getBody(String req) {
		String[] split = req.split(";");
		String[] body = new String[split.length - 1];
		for (int i = 1; i < split.length; i++) {
			body[i - 1] = split[i];
		}
		return body;
	}

	public void broadcastMessage(String message) {
		for (Channel channel : allChannels) {
			channel.writeAndFlush(message); // Envoi du message à tous les clients
		}
	}

	public class ClientHandler extends SimpleChannelInboundHandler<String> {

		@Override
		protected void channelRead0(ChannelHandlerContext ctx, String msg) {
			String header = getHeader(msg);
			String[] body = getBody(msg);

			System.out.println("Client says : " + msg);
			ctx.writeAndFlush(msg);
		}

		private void handleMenuCommands(String header, String[] body, Menu menu) {
			switch (header) {
			case "addPlayer":
				menu.addPlayer(body[0], UUID.fromString(body[1]));
				break;
			case "removePlayer":
				menu.removePlayer(UUID.fromString(body[0]));
				break;
			case "setReady":
				menu.setReady(UUID.fromString(body[0]), Boolean.parseBoolean(body[1]));
				break;
			case "setMode":
				menu.setMode(GameMode.fromString(body[0]));
				break;
			case "play":
				menu.play();
				break;
			}
		}

		private void handlePlayingCommands(String header, String[] body, Playing playing) {
			switch (header) {
			case "fire":
				playing.fire(UUID.fromString(body[0]),
						new Vector(Double.parseDouble(body[1]), Double.parseDouble(body[2])));
				break;
			case "switchMode":
				playing.switchMode(UUID.fromString(body[0]), PlayerMode.fromString(body[1]));
				break;
			}
		}

		@Override
		public void channelActive(ChannelHandlerContext ctx) throws Exception {
			allChannels.add(ctx.channel()); // Ajouter le client au groupe
		}

		@Override
		public void channelInactive(ChannelHandlerContext ctx) throws Exception {
			allChannels.remove(ctx.channel()); // Supprimer le client du groupe lorsqu'il se déconnecte
		}

		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
			cause.printStackTrace();
			ctx.close();
		}

	}
}
