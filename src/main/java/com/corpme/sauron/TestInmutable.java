package com.corpme.sauron;

import com.google.common.collect.ImmutableSet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class TestInmutable {

	final String [] list2 = new String [] {
			"Antonio",
			"Andrea",
			"Payo",
			"Pedro",
			"Seoane",
			"Delicado"};


	ImmutableSet<String> list = ImmutableSet.of("red",
			"orange",
			"yellow",
			"green",
			"blue",
			"purple");

	List<String> lista = new ArrayList<>();

	TestInmutable() {

	}

	public void add(final String s) {
		//System.out.println(Thread.currentThread().getName() + " Add: " + s);
		list = ImmutableSet.<String>builder().addAll(list).add(s).build();
	}

	public void remove(final String s) {
		System.out.println(Thread.currentThread().getName() + " Remove: " + s);
		list = ImmutableSet.<String>builder().addAll(list.stream().filter(ss -> ss != s).iterator()).build();
	}

	public void print() {
		list.forEach(s -> System.out.println(s));
	}


	public void vamos() {

		Date ini = new Date();

		final Random random = new Random();
		final int size = list2.length;
		for(int i = 0 ; i < 100000; i++) {
			final int ii = i;
				new Thread(new Runnable() {
					@Override
					public void run() {
						final int x = random.nextInt(size);
						add(list2[x]);
						System.out.println(new Date());
					}
				}).start();
		}

		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		print();
		System.out.println("Ini "+ini);
	}




	public static void main(String[] args) throws Exception {

		TestInmutable ti = new TestInmutable();
		ti.vamos();

	}
}