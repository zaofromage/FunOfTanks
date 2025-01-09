package network;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;

public class UDPServer {

    public static final int PORT = 4552;
    
    public UDPServer() {
    	new Thread(() -> {
			try {
				start();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}).start();
    }

    public void start() throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup(); // Gère les événements

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                     .channel(NioDatagramChannel.class)  // Utilisation de DatagramChannel pour UDP
                     .handler(new UdpClientHandler());

            System.out.println("Serveur UDP Netty démarré sur le port : " + PORT);
            bootstrap.bind(PORT).sync().channel().closeFuture().await();
        } finally {
            group.shutdownGracefully();
        }
    }

    public class UdpClientHandler extends SimpleChannelInboundHandler<DatagramPacket> {

        @Override
        public void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) throws Exception {
        	
        	ByteBuf buf = packet.content();
        	byte[] bytes = new byte[buf.readableBytes()];
        	buf.readBytes(bytes);
        	String msg = new String(bytes);
        	
        	System.out.println("Client UDP says : " + msg);
        	
        	ByteBuf resBuf = ctx.alloc().buffer();
        	resBuf.writeBytes(msg.getBytes());
        	
        	DatagramPacket response = new DatagramPacket(resBuf, packet.sender());
            ctx.writeAndFlush(response);
        }
        
        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            cause.printStackTrace();
            ctx.close();
        }
        
    }
}
