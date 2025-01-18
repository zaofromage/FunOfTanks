package network;

import java.util.UUID;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import model.Game;
import model.gamestate.Menu;
import model.gamestate.Playing;
import model.player.Dir;

public class UDPServer {

    public static final int PORT = 4552;
    
    private Game game;
    
    public UDPServer(Game game) {
    	this.game = game;
    	new Thread(() -> {
			try {
				start();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}).start();
    }

    public void start() throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                     .channel(NioDatagramChannel.class)
                     .handler(new UdpClientHandler());

            System.out.println("Serveur UDP Netty démarré sur le port : " + PORT);
            bootstrap.bind(PORT).sync().channel().closeFuture().await();
        } finally {
            group.shutdownGracefully();
        }
    }
    
    public void broadcastMessage(ChannelHandlerContext ctx, String msg, DatagramPacket packet) {
    	ByteBuf resBuf = ctx.alloc().buffer();
    	resBuf.writeBytes(msg.getBytes());
    	DatagramPacket response = new DatagramPacket(resBuf, packet.sender());
        ctx.writeAndFlush(response);
    }
    
    public class UdpClientHandler extends SimpleChannelInboundHandler<DatagramPacket> {

        @Override
        public void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) throws Exception {
        	
        	ByteBuf buf = packet.content();
        	byte[] bytes = new byte[buf.readableBytes()];
        	buf.readBytes(bytes);
        	String msg = new String(bytes);
        	String header = Server.getHeader(msg);
        	String[] body = Server.getBody(msg);
        	
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
        
        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            cause.printStackTrace();
            ctx.close();
        }
        
        private void handleMenuCommands(String header, String[] body, Menu menu) {
        	switch (header) {
			
        	}
        }
        
        private void handlePlayingCommands(String header, String[] body, Playing playing) {
        	switch (header) {
			case "moveTank":
				playing.moveTank(UUID.fromString(body[0]), Dir.fromString(body[1]));
				break;
			case "updateOrientation":
				playing.updateOrientation(UUID.fromString(body[0]), Integer.parseInt(body[1]), Integer.parseInt(body[2]));
				break;
        	}
        }
        
    }
}
