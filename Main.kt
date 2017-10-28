fun main (args : Array<String>){
    var tmp = ""
    args.forEach { x -> tmp += x }
    val code = "++++++++++[>+++++++>++++++++++>+++>+<<<<-]>++.>+.+++++++..+++.>++.<<+++++++++++++++.>.+++.------.--------.>+.>."


    start(code)
}

fun start(code : String){
    val loops = Loops(code)
    val mem = Memory()
    var iptr = 0

    while(iptr < code.length){
        val c = code[iptr]
        when (c){
            '+' -> mem.increase()
            '-' -> mem.decrease()
            '>' -> mem.goRight()
            '<' -> mem.goLeft()
            '.' -> print(mem.get())
            ',' -> mem.set(readChar())
            ']' -> {
                if (mem.get().toInt() != 0){
                    iptr = loops.loop(iptr)
                }
            }
        }
        iptr++
    }








    print("")
}

fun readChar() : Char{
    return readLine()!![0]
}







class Memory{
    private val mem = ArrayList<Int>()
    private var ptr : Int = 0


    init {
        mem.add(0)
    }

    fun goRight(){
        if (mem.size <= ptr + 1){
            mem.add(0)
        }
        ptr+=1
    }

    fun goLeft(){
        if(ptr - 1 < 0){
            throw IndexOutOfBoundsException("Memory pointer cannot be negative")
        }
        ptr-=1
    }

    private fun set(value : Int){
        if(value > 127){
            throw IllegalStateException("Value cannot exceed 127")
        } else if (value < 0){
            throw IllegalStateException("Value cannot be negative")
        }
        mem[ptr] = value
    }

    fun set(value : Char?){
        if(value == null){
            set(0)
        }
    }

    fun get() : Char{
        return mem[ptr].toChar()
    }

    fun increase(){
        set(mem[ptr]+1)
    }

    fun decrease(){
        set(mem[ptr]-1)
    }
}

class Loops(code : String){
    val beginning : IntArray
    val ending : IntArray
    val size : Int

    init {
        size = determineCount(code)
        beginning = IntArray(size)
        ending = IntArray(size)
        determineLoops(code)
    }

    private fun determineCount(code : String) : Int{
        var tmp = 0
        var count = 0
        for (i in code){
            when(i){
                '[' -> count+=1
                ']' -> tmp+=1
            }
        }
        if(tmp-count != 0){
            throw Exception("Loops are inconsistent")
        }
        return count
    }

    private fun determineLoops(code : String){
        var loopIndex = 0
        for ((i,v) in code.withIndex()){
            if (v == '['){
                var loopDepth = 1
                for(j in i+1..code.length-1){
                    when (code[j]){
                        '[' -> loopDepth+=1
                        ']' -> loopDepth-=1
                    }
                    if(loopDepth == 0){
                        beginning[loopIndex] = i
                        ending[loopIndex] = j
                        loopIndex+=1
                        break
                    }
                }
            }
        }
    }

    fun loop(currentIptr : Int) : Int{

        for ((i,v) in ending.withIndex()){
            if (v == currentIptr){
                return beginning[i]
            }
        }
        throw Exception("Loopindex could not be found")
    }
}