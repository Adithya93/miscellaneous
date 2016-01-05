.text
.align 2
.globl main

main:
move $t9, $0

ultraloop:
jal readName
move $t8, $v0
la $a0, list
jal readFloats
la $a0, list
jal calcScore
move $a0, $t8
la $a2, tailNode
lw $a2, 0($a2)
move $a3, $t9
jal addNode
addi $t9, $t9, 1
j ultraloop

exitMain:
beq $t9, 0, quit
la $a0, headNode
lw $a0, 0($a0)
move $a1, $t9
jal sort
move $a0, $v0
jal printAll

quit:
li $v0, 10
syscall

readName:
addi $sp, $sp, -8
sw $ra, 4($sp)
sw $s0, 0($sp)
li $v0, 9
li $a0, 60
syscall
move $a0, $v0
li $v0, 8
addi $a1, $0, 60
syscall
jal rmnl
move $a0, $v0
move $s0, $a0
jal testExit
move $v0, $s0 
lw $s0, 0($sp)
lw $ra, 4($sp)
addi $sp, $sp, 8
jr $ra

testExit:
addi $sp, $sp, -4
sw $ra, 0($sp)
la $a1, endFlag
jal strcmp
lw $ra, 0($sp)
addi $sp, $sp, 4
beq $v0, 0, exitMain
jr $ra

readFloats:
addi $sp, $sp, -8
sw $s0, 4($sp)
sw $s1, 0($sp)
move $s0, $0
move $s1, $a0
loop:
li $v0, 6
syscall
mov.s $f4, $f0
s.s $f4, 0($s1) 
addi $s1, $s1, 4
addi $s0, $s0, 1
blt $s0, 3, loop
move $v0, $a0
lw $s1, 0($sp)
lw $s0, 4($sp)
addi $sp, $sp, 8
jr $ra

calcScore:
addi $sp, $sp, -12
sw $s0, 8($sp)
sw $s1, 4($sp)
sw $s2, 0($sp)
move $s0, $0
mtc1 $0, $f4
addi $s1, $0, 2
move $s2, $a0
loop3:
l.s $f5, 0($s2)
add.s $f4, $f4, $f5
addi $s0, $s0, 1
addi $s2, $s2, 4
blt $s0, $s1, loop3
l.s $f5, 0($s2)
div.s $f0, $f4, $f5 
mov.s $f12, $f0
lw $s2, 0($sp)
lw $s1, 4($sp)
lw $s0, 8($sp)
addi $sp, $sp, 12
jr $ra

makeNode:
addi $sp, $sp, -16
sw $s0, 12($sp)
sw $s1, 8($sp)
sw $s2, 4($sp)
sw $s3, 0($sp)
move $s2, $a0
li $v0, 9
addi $a0, $0, 12
syscall
move $s1, $v0
sw $s2, 0($s1)
s.s $f12, 4($s1)
move $s0, $0
sw $s0, 8($s1)
la $s3, tailNode
sw $v0, 0($s3)
lw $s3, 0($sp)
lw $s2, 4($sp)
lw $s1, 8($sp)
lw $s0, 12($sp)
addi $sp, $sp, 16
jr $ra


addNode:
beq $a3, $0, makeHead
addi $sp, $sp, -4
sw $ra, 0($sp)
jal makeNode
add $s0, $v0, $0
addi $s1, $a2, 8
sw $s0, 0($s1)
add $v0, $s0, $0
lw $ra, 0($sp)
addi $sp, $sp, 4
jr $ra

makeHead:
addi $sp, $sp, -4
sw $ra, 0($sp)
jal makeNode
move $s0, $v0
la $s1, headNode
sw $s0, 0($s1)
lw $ra, 0($sp)
addi $sp, $sp, 4
move $v0, $s0
jr $ra

sort:
addi $sp, $sp, -40
sw $ra, 36($sp)
sw $a0, 32($sp)
sw $a1, 28($sp)
sw $s0, 24($sp)
sw $s1, 20($sp)
sw $s2, 16($sp)
sw $s3, 12($sp)
sw $s4, 8($sp)
sw $s5, 4($sp)
sw $s6, 0($sp)
li $v0, 9
sll $a0, $a1, 2
syscall
lw $a0, 32($sp)
la $s0, sortHead
sw $v0, 0($s0)
move $s0, $v0
move $s1, $0
move $s2, $s0
move $s3, $0
move $s4, $a0
move $s5, $a1

outerloop:
move $a0, $s4
move $a1, $s5
jal findMax
addi $s5, $s5, -1
move $s2, $v0
move $s3, $v1
jal modifyOld
sll $s6, $s1, 2
add $s6, $s6, $s0
sw $s2, 0($s6)

addi $s1, $s1, 1
lw $a1, 28($sp)
blt $s1, $a1, outerloop
add $v0, $s0, $0
lw $s6, 0($sp)
lw $s5, 4($sp) 
lw $s4, 8($sp)
lw $s3, 12($sp)
lw $s2, 16($sp)
lw $s1, 20($sp)
lw $s0, 24($sp)
lw $a1, 28($sp)
lw $a0, 32($sp)
lw $ra, 36($sp)
addi $sp, $sp, 40
jr $ra


modifyOld:
addi $sp, $sp, -8
sw $s0, 4($sp)
sw $s1, 0($sp) 
add $s0, $s3, $0
beq $s0, $0, changeHead
addi $s1, $s0, 8
lw $s1, 0($s1)
addi $s1, $s1, 8
lw $s1, 0($s1)
addi $s0, $s0, 8
sw $s1, 0($s0)
returnToSort:
lw $s1, 0($sp)
lw $s0, 4($sp)
addi $sp, $sp, 8
jr $ra

changeHead:
add $s0, $s2, $0
addi $s1, $s0, 8
lw $s1, 0($s1)
move $s4, $s1
j returnToSort


findMax:
addi $sp, $sp, -28
sw $s0, 24($sp)
sw $s1, 20($sp)
s.s $f4, 16($sp)
s.s $f5, 12($sp)
sw $s2, 8($sp)
sw $s3, 4($sp)
sw $s4, 0($sp)
move $s0, $a0
move $s1, $0
l.s $f4, 4($a0)
move $s2, $a0
move $s3, $0

innerloop:
l.s $f5, 4($s0)
c.lt.s $f4, $f5
bc1t setMax

moveOn:
move $s4, $s0
addi $s0, $s0, 8
addi $s1, $s1, 1
lw $s0, 0($s0)
blt $s1, $a1, innerloop
add $v0, $s2, $0
add $v1, $s3, $0
lw $s4, 0($sp)
lw $s3, 4($sp)
lw $s2, 8($sp)
l.s $f5, 12($sp)
l.s $f4, 16($sp)
lw $s1, 20($sp)
lw $s0, 24($sp)
addi $sp, $sp, 28
jr $ra

setMax:
mov.s $f4, $f5
add $s2, $s0, $0
add $s3, $s4, $0
j moveOn


printAll:
addi $sp, $sp, -24
sw $a0, 20($sp)
sw $s0, 16($sp)
sw $s1, 12($sp)
sw $s2, 8($sp)
sw $s3, 4($sp)
sw $s4, 0($sp)
move $s0, $0
move $s2, $a0
lw $s4, 0($s2)
lw $s4, 0($s4)
sillyloop:
lw $s1, 0($s2)
lw $s3, 0($s1)
li $v0, 4
move $a0, $s3
syscall
la $a0, whitespace
syscall
li $v0, 2
l.s $f12, 4($s1)
syscall
li $v0, 4
la $a0, nl
syscall
lw $a0, 12($sp)
addi $s0, $s0, 1
addi $s2, $s2, 4
blt $s0, $a1, sillyloop
li $v0, 4
move $a0, $s4
syscall
la $a0, nl
syscall
lw $s4, 0($sp)
lw $s3, 4($sp)
lw $s2, 8($sp)
lw $s1, 12($sp)
lw $s0, 16($sp)
lw $a0, 20($sp)
addi $sp, $sp, 24
jr $ra


strcmp:

cmploop:
lbu $t1, 0($a0)
lbu $t2, 0($a1)
beq $t1, 0, checkt2
addi $a0, $a0, 1
addi $a1, $a1, 1
beq $t1, $t2, cmploop
bgt $t1, $t2, greater

smaller:
addi $v0, $0, -1
j exit

greater:
addi $v0, $0, 1
j exit

equal:
addi $v0, $0, 0
j exit

checkt2:
beq $t2, 0, equal
j smaller

exit:
jr $ra

rmnl:
add $s0, $a0, $0
la $s2, nl
lbu $s2, 0($s2)
rmloop:
lbu $s1, 0($s0)
beq $s1, $s2, rm
addi $s0, $s0, 1
beq $s1, $0, exitrmnl
j rmloop

rm:
sb $0, 0($s0)
j exitrmnl

exitrmnl:
add $v0, $a0, $0
jr $ra

.data
.align 2
list: .float 0.0, 0.0, 0.0
nl: .asciiz "\n"
endFlag: .asciiz "DONE"
headNode: .word 0
tailNode: .word 0
whitespace: .asciiz " "
sortHead: .word 0
