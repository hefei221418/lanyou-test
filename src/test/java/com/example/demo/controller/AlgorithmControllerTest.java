package com.example.demo.controller;

import com.example.demo.controller.AlgorithmController.BinarySearchResult;
import com.example.demo.controller.AlgorithmController.FactorialResult;
import com.example.demo.controller.AlgorithmController.FibonacciResult;
import com.example.demo.controller.AlgorithmController.PrimeResult;
import com.example.demo.controller.AlgorithmController.SortResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AlgorithmControllerTest {

    @InjectMocks
    private AlgorithmController algorithmController;

    @Test
    void binarySearch_WhenTargetExists_ShouldReturnCorrectIndex() {
        // Given
        int[] array = {1, 3, 5, 7, 9, 11};
        int target = 7;

        // When
        ResponseEntity<BinarySearchResult> response = algorithmController.binarySearch(array, target);

        // Then
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(target, response.getBody().getTarget());
        assertEquals(3, response.getBody().getIndex()); // 7在排序后的数组中的索引
    }

    @Test
    void binarySearch_WhenTargetNotExists_ShouldReturnNegativeOne() {
        // Given
        int[] array = {1, 3, 5, 7, 9};
        int target = 4;

        // When
        ResponseEntity<BinarySearchResult> response = algorithmController.binarySearch(array, target);

        // Then
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(-1, response.getBody().getIndex());
    }

    @Test
    void quickSort_ShouldReturnSortedArray() {
        // Given
        int[] array = {64, 34, 25, 12, 22, 11, 90};

        // When
        ResponseEntity<SortResult> response = algorithmController.quickSort(array);

        // Then
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("quickSort", response.getBody().getAlgorithm());
        
        int[] sortedArray = response.getBody().getSortedArray();
        assertArrayEquals(new int[]{11, 12, 22, 25, 34, 64, 90}, sortedArray);
    }

    @Test
    void bubbleSort_ShouldReturnSortedArray() {
        // Given
        int[] array = {64, 34, 25, 12, 22, 11, 90};

        // When
        ResponseEntity<SortResult> response = algorithmController.bubbleSort(array);

        // Then
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("bubbleSort", response.getBody().getAlgorithm());
        
        int[] sortedArray = response.getBody().getSortedArray();
        assertArrayEquals(new int[]{11, 12, 22, 25, 34, 64, 90}, sortedArray);
    }

    @Test
    void fibonacci_ShouldReturnCorrectSequence() {
        // Given
        int n = 10;

        // When
        ResponseEntity<FibonacciResult> response = algorithmController.fibonacci(n);

        // Then
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(n, response.getBody().getN());
        
        List<Integer> sequence = response.getBody().getSequence();
        assertEquals(10, sequence.size());
        assertEquals(List.of(0, 1, 1, 2, 3, 5, 8, 13, 21, 34), sequence);
    }

    @Test
    void fibonacci_WhenNegativeInput_ShouldReturnBadRequest() {
        // Given
        int n = -1;

        // When
        ResponseEntity<FibonacciResult> response = algorithmController.fibonacci(n);

        // Then
        assertEquals(400, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    void primeNumbers_ShouldReturnCorrectPrimes() {
        // Given
        int limit = 30;

        // When
        ResponseEntity<PrimeResult> response = algorithmController.primeNumbers(limit);

        // Then
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(limit, response.getBody().getLimit());
        
        List<Integer> primes = response.getBody().getPrimes();
        assertEquals(List.of(2, 3, 5, 7, 11, 13, 17, 19, 23, 29), primes);
    }

    @Test
    void primeNumbers_WhenLimitLessThan2_ShouldReturnEmptyList() {
        // Given
        int limit = 1;

        // When
        ResponseEntity<PrimeResult> response = algorithmController.primeNumbers(limit);

        // Then
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getPrimes().isEmpty());
    }

    @Test
    void factorial_ShouldReturnCorrectResult() {
        // Given
        int n = 5;

        // When
        ResponseEntity<FactorialResult> response = algorithmController.factorial(n);

        // Then
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(n, response.getBody().getN());
        assertEquals(120, response.getBody().getResult()); // 5! = 120
    }

    @Test
    void factorial_WhenZero_ShouldReturnOne() {
        // Given
        int n = 0;

        // When
        ResponseEntity<FactorialResult> response = algorithmController.factorial(n);

        // Then
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getResult()); // 0! = 1
    }

    @Test
    void factorial_WhenNegativeInput_ShouldReturnBadRequest() {
        // Given
        int n = -1;

        // When
        ResponseEntity<FactorialResult> response = algorithmController.factorial(n);

        // Then
        assertEquals(400, response.getStatusCodeValue());
        assertNull(response.getBody());
    }
}