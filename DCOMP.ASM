;--------------------------------------------------------------------------
;
;   Program: dcomp
;   Author: Vincent Bugica
;   Date: March 22, 2011
;
;   Function:  Dcomp decompresses ASCII text.
;
;   Input:
;   - si points to the string of compressed data
;   - di points to an empty list into which the decompressed data is stored
;
;   Output:
;
;   - The compressed data in inlist is decompressed into output list
;   - The compressed data is not modified
;   - All registers contain their original value except ax
;     ax = 0       Invalid compressed bit pattern found
;     ax = 1...n   okay and n = size of decompressed data
;
;--------------------------------------------------------------------------


;----------------------------------
         .model    small          ;64k code and 64k data
         .8086                    ;only allow 8086 instructions
         public    dcomp          ;allow linker to access dcomp
         public    getbit         ;allow linker to access getbit
;----------------------------------

        .data
;-------------------------------------------------------------
tbl1     db        ' ETA'                                    ; 0X table
         db          44 dup('*')                             ; Invalid
         db        '*1234567890.',13,10,'*',26               ; 11x table
         db        'BCDFGHIJKLMNOPQRSUVWXYZ'                 ; 10x
         db        213 dup('*')                              ; Invalid characters
varl     dw            ?                                     ; Length of output
varb     dw            ?                                     ; Current bit
;-------------------------------------------------------------

         .code
;----------------------------------
dcomp:                            ;
         push bx                  ; Save bx - current loop count and table ptr
         push cx                  ; Save cx - current index (ch) and total # loops (cl)
         push di                  ; Save DI - output buffer
                                  ; 
         mov bx,0                 ; Initialize BX
         mov [varb],-1            ; Initialize current bit
         mov [varl],0             ; Initalize length
         mov cx,3                 ; Initial 3 bits   (ch = 00, cl = 03) 
;----------------------------------
;DLOOP:
;Loops getbit for X amount of bits
;----------------------------------
dloop:                            ;
         inc  [varb]              ; Increment 'B', current bit
         shl  bl,1                ; Shift the index over; (used in context when the bit is not the first loop)
         mov  ax,[varb]           ; Move 'B' to AX
         call getbit              ; Go to getbit
                                  ;
         add  bl,al               ; Add the bit into the index
         loop dloop               ; Loop until CX = 0
                                  ;
         cmp  bl,3                ; Is it in the 0x segment?
         jbe  indx                ; If so, get the bit from the index
                                  ;
         cmp  bl,5                ; Is it in the 10x segment?
         jbe  onex                ; If so, jump to that label
                                  ;
         cmp  bl,7                ; Is it in the 11x segment?
         jbe  twox                ; If so jump to that label
                                  ;
                                  ; Else, it's receieved all its bits
;---------------------------------------
; Indx:
; Retrieves character from table
; Via indirect addressing
;---------------------------------------
indx:                                  ;
         mov  al,bl                    ; Move index to Al
         mov  bx,offset tbl1           ; BX points to the table
         xlat                          ; Moves char to al register
         cmp  al,'*'                   ; Is it an invalid character?
         je   invl                     ; If so, terminate
                                       ;
         inc  [varl]                   ; Else, increment output length
         mov  byte ptr [di],al         ; Move char into output string
         inc  di                       ; Move di over
                                       ;
         cmp  al,26                    ; Is the char an EOF?
         je   quet                     ; If so, exit
                                       ;
                                       ; Else, reset and get the next char
;---------------------------------------
; Nxtc:
; Reset vars and get next bits
;----------------------------------
nxtc:                             ;
         mov  bx,0                ; Resets loop iterations
;----------------------------------
twox:                             ;
         mov  cx,3                ; Set loop counter
         jmp  dloop               ; Get last bits
;----------------------------------
; Onex:
; Located in 10x segment,
; thus requiring 4 more bits
;----------------------------------
onex:                             ;
         mov  cx,4                ; Set loop counter
         jmp  dloop               ; Get last bits
;----------------------------------
; Twox:
; Located in 11x segment,
; thus requiring 3 more bits
;----------------------------------

; Invl:
; Invalid character:
; Set ax to zero and terminate
;----------------------------------                       
invl:                             ;
         mov  [varl],0            ; Invalid: ax will be zero
                                  ; Exit
;----------------------------------
; Received EOF - return to main
;-------------------------------------------------
quet:                                            ;
         mov  ax,[varl]                          ; Move number of bytes to ax
         pop  di                                 ; Return DI
         pop  cx                                 ; Return CX register 
         pop  bx                                 ; Return BX register
         ret                                     ; Return to main
;-------------------------------------------------


         .data
;----------------------------------
vare     db        8              ; Eight
;----------------------------------


;----------------------------------
; GETBIT
;
; Input:
; - ax contains the bit number requested (0 to 1783 are valid)
; - si points to the input compressed data string 1 to 223 bytes
;
; Output
; - if the requested bit number is valid (0 to 1783) then
;   ax contains the bit value 0-1
; - if the requested bit number is invalid (greater than 1783) then
;   ax is unchanged
; - the subroutine must save and restore any other used register
; - the input compressed data may not be changed
;----------------------------------
         .code
;----------------------------------
getbit:                           ;
         push cx                  ; Save cx register
         push bx                  ; Save bx register
         div  [vare]              ; Break AX into bytes (R = ah, Q = al)
         mov  bl,al               ; Save Quotient
         mov  cl,ah               ; Move the remainder (bits) into cl
         mov  al,[si+bx]          ; Move pointer to closest byte and place the value into AL
         rol  al,cl               ; Rotate the desired bit to carry
         mov  cl,7                ; Plan on shifting most sig to least sig
         shr  al,cl               ; Shift
;---------------------------------
; Return the stack/params to main
;---------------------------------
exit:                             ;
         pop bx                   ; Return bx register
         pop cx                   ; Return cx register
         ret                      ; Go back to main program
;----------------------------------
        end 

