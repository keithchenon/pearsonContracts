package com.example.demo;

import io.javalin.Javalin;

public class DemoApplication {

	public static void main(String[] args) {
		new DemoApplication().run(7000);
	}

	public Javalin start(int port) {
		return Javalin.create().start(port);
	}

	public Javalin registerGet(Javalin app) {
		return app.get("/", ctx -> ctx.result("Hello World"));
	}

	public Javalin run(int port) {
		return registerGet(start(port));
	}

}
