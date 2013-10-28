package com.tint.specular.states;

import java.io.IOException;
import java.net.InetAddress;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.tint.specular.Specular;

public class NetworkTest extends State {

	private BitmapFont font = new BitmapFont();
	private Client client;
	private Server server;
	private long receivedTime;
	
	public NetworkTest(Specular game) {
		super(game);
		client = new Client();
		register(client.getKryo());
		client.addListener(new Listener() {
			@Override
			public void received(Connection connection, Object object) {
				super.received(connection, object);
				if(object instanceof HelloPacket)
					receivedTime = System.currentTimeMillis();
			}
		});
		try {
			client.start();
			InetAddress host = client.discoverHost(21212, 1000);
			if(host != null) {
				client.connect(1000, host, 21211, 21212);
			} else {
				startServerAndConnect();
			}
		} catch (IOException e) {
			startServerAndConnect();
		}
		System.out.println(server);
		System.out.println(client.isConnected());
		Timer.schedule(new Task() {
			@Override
			public void run() {
				client.updateReturnTripTime();
			}
		}, 0, 500);
	}
	
	private void register(Kryo kryo) {
		kryo.register(HelloPacket.class);
	}
	
	private void startServerAndConnect() {
		server = new Server();
		register(server.getKryo());
		server.addListener(new Listener() {
			@Override
			public void received(Connection connection, Object object) {
				super.received(connection, object);
				if(object instanceof HelloPacket)
					server.sendToAllExceptTCP(connection.getID(), object);
			}
		});
		try {
			server.bind(21211, 21212);
			server.start();
			client.connect(1000, "127.0.0.1", 21211, 21212);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.2f, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		game.camera.update();
		game.batch.setProjectionMatrix(game.camera.combined);
		
		game.batch.begin();
		font.draw(game.batch, (server != null ? "Localhost" : "Ping: " + client.getReturnTripTime()), 0, 0);
		if(System.currentTimeMillis() - receivedTime < 1000) {
			font.draw(game.batch, "Hello!", 0, -50);
		}
		game.batch.end();
		
		if(Gdx.input.justTouched())
			client.sendTCP(new HelloPacket());
	}
	
	private static class HelloPacket {
	}
}
