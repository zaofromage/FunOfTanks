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
import model.Game;
import model.gamestate.GameMode;
import model.gamestate.Menu;
import model.gamestate.Playing;
import model.player.Dir;
import model.player.PlayerMode;
import utils.Vector;

public class Server {

	public static final int PORT = 4551;
	private ChannelGroup allChannels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
	private EventLoopGroup bossGroup;
	private EventLoopGroup workerGroup;
	
	private UDPServer us;
	
	private Game game;

	public Server() {
		game = new Game();
		new Thread(() -> {
			try {
				start();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}).start();
	}

	public void start() throws InterruptedException {
		bossGroup = new NioEventLoopGroup();
		workerGroup = new NioEventLoopGroup();
		us = new UDPServer(game);
		try {
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch) {
							ch.pipeline().addLast(new StringDecoder());
							ch.pipeline().addLast(new StringEncoder());
							ch.pipeline().addLast(new ClientHandler());
						}
					}).option(ChannelOption.SO_BACKLOG, 128) // File d'attente
					.childOption(ChannelOption.SO_KEEPALIVE, true).childOption(ChannelOption.TCP_NODELAY, true);
			System.out.println("Serveur Netty démarré sur le port : " + PORT);
			bootstrap.bind(PORT).sync().channel().closeFuture().sync();
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
	
	public void shutdown() {
	    System.out.println("Arrêt du serveur Netty...");

	    try {
	        allChannels.close().sync();
	        System.out.println("Tous les clients déconnectés.");

	        bossGroup.shutdownGracefully().sync();
	        workerGroup.shutdownGracefully().sync();
	        System.out.println("Groupes d'événements arrêtés.");
	    } catch (InterruptedException e) {
	        e.printStackTrace();
	        Thread.currentThread().interrupt();
	    }

	    System.out.println("Serveur arrêté proprement.");
	}


	public class ClientHandler extends SimpleChannelInboundHandler<String> {

		@Override
		protected void channelRead0(ChannelHandlerContext ctx, String msg) {
			String header = getHeader(msg);
			String[] body = getBody(msg);
			switch (game.getState()) {
			case MENU:
				handleMenuCommands(header, body, game.getMenu());
				break;
			case PLAYING:
				handlePlayingCommands(header, body, game.getPlaying());
				break;
			case FINISH:
				break;
			}
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
	
	public Game getGame() {
		return game;
	}
}
