package com.example.demo.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/algorithms")
@RequiredArgsConstructor
public class AlgorithmController {

    @GetMapping("/binarySearch")
    public ResponseEntity<BinarySearchResult> binarySearch(@RequestParam int[] array, @RequestParam int target) {
        int result = performBinarySearch(array, target);
        return ResponseEntity.ok(new BinarySearchResult(array, target, result));
    }

    @GetMapping("/quickSort")
    public ResponseEntity<SortResult> quickSort(@RequestParam int[] array) {
        int[] sortedArray = array.clone();
        performQuickSort(sortedArray, 0, sortedArray.length - 1);
        return ResponseEntity.ok(new SortResult(array, sortedArray, "quickSort"));
    }

    @GetMapping("/bubbleSort")
    public ResponseEntity<SortResult> bubbleSort(@RequestParam int[] array) {
        int[] sortedArray = array.clone();
        performBubbleSort(sortedArray);
        return ResponseEntity.ok(new SortResult(array, sortedArray, "bubbleSort"));
    }

    @GetMapping("/fibonacci")
    public ResponseEntity<FibonacciResult> fibonacci(@RequestParam int n) {
        if (n < 0) {
            return ResponseEntity.badRequest().body(null);
        }
        List<Integer> sequence = generateFibonacci(n);
        return ResponseEntity.ok(new FibonacciResult(n, sequence));
    }

    @GetMapping("/primeNumbers")
    public ResponseEntity<PrimeResult> primeNumbers(@RequestParam int limit) {
        if (limit < 2) {
            return ResponseEntity.ok(new PrimeResult(limit, new ArrayList<>()));
        }
        List<Integer> primes = generatePrimes(limit);
        return ResponseEntity.ok(new PrimeResult(limit, primes));
    }

    @GetMapping("/factorial")
    public ResponseEntity<FactorialResult> factorial(@RequestParam int n) {
        if (n < 0) {
            return ResponseEntity.badRequest().body(null);
        }
        long result = calculateFactorial(n);
        return ResponseEntity.ok(new FactorialResult(n, result));
    }

    // 二分查找算法
    private int performBinarySearch(int[] array, int target) {
        Arrays.sort(array); // 确保数组有序
        int left = 0;
        int right = array.length - 1;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            
            if (array[mid] == target) {
                return mid;
            }
            
            if (array[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        
        return -1; // 未找到
    }

    // 快速排序算法
    private void performQuickSort(int[] array, int low, int high) {
        if (low < high) {
            int pi = partition(array, low, high);
            performQuickSort(array, low, pi - 1);
            performQuickSort(array, pi + 1, high);
        }
    }

    private int partition(int[] array, int low, int high) {
        int pivot = array[high];
        int i = (low - 1);
        
        for (int j = low; j < high; j++) {
            if (array[j] <= pivot) {
                i++;
                // 交换 array[i] 和 array[j]
                int temp = array[i];
                array[i] = array[j];
                array[j] = temp;
            }
        }
        
        // 交换 array[i+1] 和 array[high]
        int temp = array[i + 1];
        array[i + 1] = array[high];
        array[high] = temp;
        
        return i + 1;
    }

    // 冒泡排序算法
    private void performBubbleSort(int[] array) {
        int n = array.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (array[j] > array[j + 1]) {
                    // 交换 array[j] 和 array[j+1]
                    int temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                }
            }
        }
    }

    // 斐波那契数列
    private List<Integer> generateFibonacci(int n) {
        List<Integer> sequence = new ArrayList<>();
        if (n == 0) return sequence;
        
        sequence.add(0);
        if (n == 1) return sequence;
        
        sequence.add(1);
        for (int i = 2; i < n; i++) {
            sequence.add(sequence.get(i - 1) + sequence.get(i - 2));
        }
        return sequence;
    }

    // 生成素数
    private List<Integer> generatePrimes(int limit) {
        List<Integer> primes = new ArrayList<>();
        for (int i = 2; i <= limit; i++) {
            if (isPrime(i)) {
                primes.add(i);
            }
        }
        return primes;
    }

    private boolean isPrime(int n) {
        if (n <= 1) return false;
        if (n <= 3) return true;
        if (n % 2 == 0 || n % 3 == 0) return false;
        
        for (int i = 5; i * i <= n; i += 6) {
            if (n % i == 0 || n % (i + 2) == 0) {
                return false;
            }
        }
        return true;
    }

    // 计算阶乘
    private long calculateFactorial(int n) {
        if (n == 0 || n == 1) return 1;
        long result = 1;
        for (int i = 2; i <= n; i++) {
            result *= i;
        }
        return result;
    }

    // 结果类定义
    @Data
    static class BinarySearchResult {
        private final int[] originalArray;
        private final int target;
        private final int index;
    }

    @Data
    static class SortResult {
        private final int[] originalArray;
        private final int[] sortedArray;
        private final String algorithm;
    }

    @Data
    static class FibonacciResult {
        private final int n;
        private final List<Integer> sequence;
    }

    @Data
    static class PrimeResult {
        private final int limit;
        private final List<Integer> primes;
    }

    @Data
    static class FactorialResult {
        private final int n;
        private final long result;
    }
}