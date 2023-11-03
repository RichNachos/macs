// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Fill.asm

// Runs an infinite loop that listens to the keyboard input.
// When a key is pressed (any key), the program blackens the screen,
// i.e. writes "black" in every pixel;
// the screen should remain fully black as long as the key is pressed. 
// When no key is pressed, the program clears the screen, i.e. writes
// "white" in every pixel;
// the screen should remain fully clear as long as no key is pressed.

// Put your code here.

// PSEUDOCODE:
// Checks for @KBD value and jumps to either PRESSED or NOTPRESSED
// in PRESSED, we save a value -1 to overwrite all the screen bits with
// -1 corresponds to 1111 1111 1111 1111 bits in word form, so every pixel should turn black
// in NOTPRESSED we save 0 instead of -1 to represent 0000 0000 0000 0000, so every pixel should turn white
// we save this value into RAM[bit]
// Then we jump to FILLWITHBIT which runs a loop to fill all 8192 screen words with the saved RAM[bit] value.
// While running this loop we do not check if the keyboard is unpressed or pressed again until we fill
// the screen with the saved value.
// There is only a small window of time in which the keyboard is actually checked but due
// to the fact that the program is very fast it does not matter.
// After finishing the LOOP in FILLWITHBIT we jump back to KEYBOARDCHECK to check for keyboard press again.

(KEYBOARDCHECK)
    @KBD
    D=M  // Store keyboard value into D
    @PRESSED
    D;JGT // if D > 0 go to PRESSED
    @NOTPRESSED
    0;JMP // else go to NOTPRESSED

(PRESSED)
    @bit
    M=-1 // save 1111 1111 1111 1111
    @FILLWITHBIT
    0;JMP // jump to filling screen with @bit

(NOTPRESSED)
    @bit
    M=0 // save 0000 0000 0000 0000
    @FILLWITHBIT
    0;JMP // jump to filling screen with @bit

(FILLWITHBIT)
    @i
    M=0
    (LOOP)
        @SCREEN
        D=A  //Store screen start word into D
        @i
        D=D+M  //current pointer = screen start + RAM[i] (offset)
        @ptr
        M=D  //store current pointer to RAM[ptr]
        @bit
        D=M  //load bit with which to fill screen into D
        @ptr
        A=M  //load RAM[ptr] into A registry
        M=D  //finally fill the RAM[SCREEN + i (or ptr for short)] with saved bit
        @i
        M=M+1  // i = i + 1
        D=M // D = i
        @8191 // load 8191 into A
        D=D-A // i = i - 8191
        @KEYBOARDCHECK
        D;JGT // if (i-512) > 0 stop filling and check keyboard again
        @LOOP
        0;JMP // else continue filling

