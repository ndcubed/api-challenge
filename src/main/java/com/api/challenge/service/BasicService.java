package com.api.challenge.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public abstract class BasicService {
    private RestTemplate restTemplate;

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Processes a List of Callable requests using an ExecutorService and returns
     * on error of one request or successful completion of all requests.
     *
     * @param callables List of Callable requests to process in parallel.
     * @param <T>       Expected return type from Callable.call()
     * @return List of type T
     */
    protected <T> List<T> processRequests(List<Callable<T>> callables) throws InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();
        List<Future<T>> results = executorService.invokeAll(callables);
        executorService.shutdown();
        return results.stream().map(doTry(Future::get)).collect(Collectors.toList());
    }

    /**
     * Retrieves a given Object using a GET request from the given
     * requestUrl.
     *
     * @param requestUrl The GET endpoint.
     * @param type       The expected response type.
     * @param <T>        Return type.
     * @return Object of type T.
     */
    protected <T> T get(String requestUrl, Class<T> type) {
        return restTemplate.getForObject(requestUrl, type);
    }

    /**
     * Wraps the given CheckedMethod apply function in a try
     * catch to catch and throw any errors during execution.
     * This is useful for use in a lambda expression to allow for
     * concise syntax.
     */
    protected <T, R> Function<T, R> doTry(CheckedMethod<T, R> checkedFunction) {
        return t -> {
            try {
                return checkedFunction.apply(t);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

    /**
     * Creates a list of Callable Objects for use when performing batch requests
     * through the use of separate threads.
     *
     * @param requests BasicRequest that should be ran on execution.
     * @param <T>      Expected return type of BasicRequest.
     * @return List of Callable.
     */
    protected <T> List<Callable<T>> createCallables(List<BasicRequest<T>> requests) {
        return requests.stream().map(request -> (Callable<T>) request::get).collect(Collectors.toList());
    }
}
