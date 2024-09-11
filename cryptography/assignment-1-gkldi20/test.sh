#!/bin/bash

NUM_CHALLENGES=6
NUM_TESTCASES=5
# Define the directory where your Python programs are located
PROGRAM_DIR="."

# Define the base directory where your test cases are located
TESTS_BASE_DIR="./tests"

all_tests_passed=true

# Iterate through each challenge and test directory
for challenge_num in $(seq 1 "$NUM_CHALLENGES"); do
    CHALLENGE_DIR="challenge$challenge_num"
    TEST_DIR="$TESTS_BASE_DIR/tests_$challenge_num"

    # Function to run a single test case
    run_test() {
        test_case="$1"
        input_file="$TEST_DIR/in_$test_case.txt"
        expected_output_file="$TEST_DIR/out_$test_case.txt"

        # Read input from the input file
        input_data=$(cat "$input_file")

        # Run the current Python program and capture the output
        actual_output=$(python3 "$PROGRAM_DIR/challenge$challenge_num.py" <<< "$input_data")

        # Read the expected output from the expected_output file
        expected_output=$(cat "$expected_output_file")

        # Compare the actual and expected output
        if [ "$actual_output" = "$expected_output" ]; then
            echo "Challenge $challenge_num - Test case $test_case: PASSED"
        else
            echo "Challenge $challenge_num - Test case $test_case: FAILED"
             echo $actual_output
             echo $expected_output
            all_tests_passed=false
        fi
    }

    echo "Running tests for Challenge $challenge_num"

    # Loop through each test case for the current challenge
    for test_case in $(seq 0 "$((NUM_TESTCASES - 1))"); do
        run_test "$test_case"
    done
done

if [ "$all_tests_passed" = true ]; then
    echo "All tests passed"
else
    echo "Some tests failed"
fi
