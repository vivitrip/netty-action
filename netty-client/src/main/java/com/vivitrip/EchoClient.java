package com.vivitrip;

import com.vivitrip.Handler.EchoClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import java.net.InetSocketAddress;

/**
 * @author : xiaowei
 * @date: 2022/7/8
 */
public class EchoClient {

  private final String host;
  private final int port;

  public EchoClient(String host, int port) {
    this.host = host;
    this.port = port;
  }

  public static void main(String[] args) throws Exception {
    if (args.length != 2) {
      System.err.println("Usgae:" + EchoClient.class.getSimpleName() + "<host><port>");
    }
    String host = args[0];
    int port = Integer.parseInt(args[1]);
    new EchoClient(host, port).start();
  }

  private void start() throws Exception {
    EventLoopGroup group = new NioEventLoopGroup();
    try {
      Bootstrap bootstrap = new Bootstrap();
      bootstrap.group(group).channel(NioSocketChannel.class)
          .remoteAddress(new InetSocketAddress(host, port)).handler(
              new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) {
                  socketChannel.pipeline().addLast(new EchoClientHandler());
                }
              });
      ChannelFuture future = bootstrap.connect().sync();
      future.channel().closeFuture().sync();
    } finally {
      group.shutdownGracefully().sync();
    }
  }
}