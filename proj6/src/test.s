	.data
	.align 4
a:	.word 0
	.text
	.globl main
main:
__start:
	sw    $ra, 0($sp)
	subu  $sp, $sp, 4
	sw    $fp, 0($sp)
	subu  $sp, $sp, 4
	subu  $sp, $sp, 0
	addu  $fp, $sp, 8
_main_Exit:
	lw    $ra, 0($fp)
	move  $t0, $fp
	lw    $fp, -4($fp)
	move  $sp, $t0
	li    $v0, 10
	syscall
