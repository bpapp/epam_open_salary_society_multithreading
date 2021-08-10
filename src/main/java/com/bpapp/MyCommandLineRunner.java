package com.bpapp;

import com.bpapp.entiy.Employee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Component
@Order(1)
@Slf4j
public class MyCommandLineRunner implements CommandLineRunner {

    private final EmployeeOperationsImpl employeeOperations;

    public MyCommandLineRunner(EmployeeOperationsImpl employeeOperations) {
        this.employeeOperations = employeeOperations;
    }

    @Override
    public void run(String... args) throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        ExecutorService executorServiceSecond = Executors.newFixedThreadPool(4);

        // get list asynch
        CompletableFuture.supplyAsync(() -> employeeOperations.hiredEmployees(), executorService)
                .thenCompose(employees -> {
                    List<CompletableFuture<Employee>> combinationList = employees.stream()
                            .map(employee ->
                                    CompletableFuture.supplyAsync(() -> {
                                        System.out.println(Thread.currentThread().getId() + " - " + Thread.currentThread().getName() +
                                                "is getting " +
                                                "salary now for " + employee.getId());
                                        return employeeOperations.getSalary(employee.getId());
                                    }, executorServiceSecond).thenApplyAsync(actualSalary -> new Employee(employee.getId(), employee.getName(),
                                            actualSalary))).
                                    collect(Collectors.toList());


                    CompletableFuture<Employee>[] combinationArray = combinationList.toArray(new CompletableFuture[combinationList.size()]);

                    return CompletableFuture.allOf(combinationArray).thenRunAsync(() -> Arrays.stream(combinationArray)
                            .map(CompletableFuture::join)
                            .collect(Collectors.toList()));
                }).join();

//        results.stream().forEach(System.out::println);

        executorService.shutdown();
        executorServiceSecond.shutdown();
        log.info("asynch run ended");
        runWithOnThread();
    }

    public void runWithOnThread() {
        StopWatch watch = new StopWatch();
        watch.start();
        log.info("running started with one thread");
        employeeOperations.hiredEmployees().stream().map(i ->
                new Employee(i.getId(), i.getName(), employeeOperations.getSalary(i.getSalary())))
                .forEach(System.out::println);
        watch.stop();
        log.info("running ended with one thread in " + watch.getTotalTimeMillis() + " msecs");
    }

}
