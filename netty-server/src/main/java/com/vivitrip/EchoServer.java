package com.vivitrip;

import com.vivitrip.handler.EchoServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import java.net.InetSocketAddress;

/**
 * @author : xiaowei
 * @date: 2022/7/8
 */
public class EchoServer {

  private final int port;


  public EchoServer(int port) {
    this.port = port;
  }

  public static void main(String[] args) throws Exception {
    if (args.length != 1) {
      System.err.println("can't gat server port to start");
      return;
    }
    int port = Integer.getInteger(args[0]);
    new EchoServer(port).start();
  }

  public void start() throws Exception {

    final EchoServerHandler serverHandler = new EchoServerHandler();
    EventLoopGroup group = new NioEventLoopGroup();
    try {
      ServerBootstrap bootstrap = new ServerBootstrap();
      bootstrap.group(group).channel(NioServerSocketChannel.class)
          .localAddress(new InetSocketAddress(port))
          .childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) {
              socketChannel.pipeline().addLast(serverHandler);
            }
          });

      ChannelFuture future = bootstrap.bind().sync();
      future.channel().closeFuture().sync();
    } finally {
      group.shutdownGracefully().sync();
    }

  }
}
