package pan.lib.baseandroidframework.java_demo.io_demo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class NioEchoServer {

    public static void main(String[] args) throws IOException {
        // 创建ServerSocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false); // 设置为非阻塞模式

        // 绑定端口
        serverSocketChannel.bind(new InetSocketAddress(8080));

        // 创建Selector
        Selector selector = Selector.open();

        // 将ServerSocketChannel注册到Selector，监听接受连接事件
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        System.out.println("服务器启动，等待连接...");

        while (true) {
            // 选择就绪的通道
            if (selector.select() > 0) {
                Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();

                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();

                    // 处理新接入的连接
                    if (key.isAcceptable()) {
                        SocketChannel clientChannel = serverSocketChannel.accept();
                        clientChannel.configureBlocking(false);
                        clientChannel.register(selector, SelectionKey.OP_READ);
                        System.out.println("新连接接入: " + clientChannel);
                    }

                    // 读取客户端发送的数据
                    if (key.isReadable()) {
                        readDataFromClient(key);
                    }

                    keyIterator.remove(); // 处理完后从集合中移除
                }
            }
        }
    }

    private static void readDataFromClient(SelectionKey key) throws IOException {
        SocketChannel clientChannel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int bytesRead = clientChannel.read(buffer);

        if (bytesRead > 0) {
            buffer.flip();
            byte[] data = new byte[buffer.remaining()];
            buffer.get(data);
            String message = new String(data).trim();
            System.out.println("收到消息: " + message);

            // 回显消息给客户端
            ByteBuffer outBuffer = ByteBuffer.wrap(message.getBytes());
            clientChannel.write(outBuffer);
            buffer.clear();
        } else {
            // 关闭连接
            clientChannel.close();
        }
    }
}
