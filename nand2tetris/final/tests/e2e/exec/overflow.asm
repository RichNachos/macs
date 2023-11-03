// Tests integer overflow handling
// Results in RAM[0] integer overflowing and final state being '0'

@32767
D=A
@0
M=D
@32767
D=A
@0
M=D+M
@0
D=A
@0
M=D+M
