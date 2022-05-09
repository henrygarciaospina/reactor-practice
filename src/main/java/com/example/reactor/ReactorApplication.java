package com.example.reactor;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static java.time.Duration.ofSeconds;
import static java.util.stream.Collectors.toList;

@SpringBootApplication
public class ReactorApplication {

	public static void main(String[] args) {

		List<Integer> elements = new ArrayList<>();

		Flux.just(1 , 2, 3, 4)
				.log()
				.subscribe();

		List<Integer> collected = Stream.of(1, 2, 3, 4)
				.collect(toList());

		Flux.just(1, 2, 3, 4)
				.log()
				.map(i -> i * 2)
				.subscribe(elements::add);

		ConnectableFlux<Object> publish = Flux.create(fluxSink -> {
					while(true) {
						fluxSink.next(System.currentTimeMillis());
					}
				})
				.publish();
		publish.subscribe(System.out::println);

		ConnectableFlux<Object> publishInTwoSeconds = Flux.create(fluxSink -> {
					while(true) {
						fluxSink.next(System.currentTimeMillis());
					}
				})
				.sample(ofSeconds(2))
				.publish();
		publishInTwoSeconds.subscribe(System.out::println);

		Flux.just(1, 2, 3, 4)
				.log()
				.map(i -> i * 2)
				.subscribeOn(Schedulers.parallel())
				.subscribe(elements::add);
	}

}
