package com.cip.conwaylife3d

/** Canonical hash-consed quadtree with memoized centered Conway evolution. */
class HashLife {
    private class Node(val level:Int, val nw:Node?=null, val ne:Node?=null,
                       val sw:Node?=null, val se:Node?=null, val alive:Boolean=false) {
        val population:Long = if(level==0) (if(alive) 1L else 0L)
            else nw!!.population+ne!!.population+sw!!.population+se!!.population
    }
    private data class Key(val a:Node,val b:Node,val c:Node,val d:Node)
    private data class Evolution(val n:Node,val steps:Int)
    private val intern=HashMap<Key,Node>()
    private val cache=HashMap<Evolution,Node>()
    private val blanks=ArrayList<Node>()
    private val dead=Node(0)
    private val live=Node(0,alive=true)
    private fun quad(a:Node,b:Node,c:Node,d:Node)=intern.getOrPut(Key(a,b,c,d)){Node(a.level+1,a,b,c,d)}
    private fun blank(level:Int):Node {
        while(blanks.size<=level) blanks.add(if(blanks.isEmpty())dead else blanks.last().let{quad(it,it,it,it)})
        return blanks[level]
    }
    private fun center(n:Node)=quad(n.nw!!.se!!,n.ne!!.sw!!,n.sw!!.ne!!,n.se!!.nw!!)
    /** Return the central half after exactly steps, without wrapping. */
    private fun evolve(n:Node,steps:Int):Node {
        require(n.level>=2 && steps>=0 && steps <= (1 shl (n.level-2)))
        if(n.population==0L)return blank(n.level-1)
        if(steps==0)return center(n)
        return cache.getOrPut(Evolution(n,steps)){
            if(n.level==2){
                val grid=Array(4){BooleanArray(4)}
                fun read(q:Node,x:Int,y:Int){
                    grid[y][x]=q.nw!!.alive;grid[y][x+1]=q.ne!!.alive
                    grid[y+1][x]=q.sw!!.alive;grid[y+1][x+1]=q.se!!.alive
                }
                read(n.nw!!,0,0);read(n.ne!!,2,0);read(n.sw!!,0,2);read(n.se!!,2,2)
                fun result(x:Int,y:Int):Node {
                    var count=0
                    for(dy in -1..1)for(dx in -1..1)if((dx!=0||dy!=0)&&grid[y+dy][x+dx])count++
                    return if(count==3||(count==2&&grid[y][x]))live else dead
                }
                quad(result(1,1),result(2,1),result(1,2),result(2,2))
            } else {
                val a=n.nw!!;val b=n.ne!!;val c=n.sw!!;val d=n.se!!
                val pieces=listOf(a,quad(a.ne!!,b.nw!!,a.se!!,b.sw!!),b,
                    quad(a.sw!!,a.se!!,c.nw!!,c.ne!!),center(n),
                    quad(b.sw!!,b.se!!,d.nw!!,d.ne!!),c,
                    quad(c.ne!!,d.nw!!,c.se!!,d.sw!!),d)
                val first=minOf(steps,1 shl(n.level-3))
                val second=steps-first
                val p=pieces.map{evolve(it,first)}
                fun square(i:Int,j:Int,k:Int,l:Int)=quad(p[i],p[j],p[k],p[l])
                quad(evolve(square(0,1,3,4),second),evolve(square(1,2,4,5),second),
                     evolve(square(3,4,6,7),second),evolve(square(4,5,7,8),second))
            }
        }
    }
    /** Exact advance, using memoized multi-generation steps. */
    fun advance(input:Set<Cell>,steps:Int):Set<Cell> {
        require(steps in 0..1_000_000)
        if(steps==0||input.isEmpty())return input
        val lowX=input.minOf{it.x.toLong()}-steps
        val highX=input.maxOf{it.x.toLong()}+steps
        val lowY=input.minOf{it.y.toLong()}-steps
        val highY=input.maxOf{it.y.toLong()}+steps
        var level=2
        val span=maxOf(highX-lowX+1,highY-lowY+1)
        while(level<29 && ((1L shl(level-1))<span||(1L shl(level-2))<steps))level++
        require(level<29){"World too large"}
        val side=1L shl level
        val ox=(lowX+highX)/2-side/2
        val oy=(lowY+highY)/2-side/2
        val positions=input.map{Pair(it.x.toLong()-ox,it.y.toLong()-oy)}
        fun build(level:Int,x:Long,y:Long,points:List<Pair<Long,Long>>):Node {
            if(points.isEmpty())return blank(level)
            if(level==0)return live
            val half=1L shl(level-1)
            val groups=Array(4){ArrayList<Pair<Long,Long>>()}
            for(point in points){
                val dx=if(point.first>=x+half)1 else 0
                val dy=if(point.second>=y+half)1 else 0
                groups[2*dy+dx].add(point)
            }
            return quad(build(level-1,x,y,groups[0]),build(level-1,x+half,y,groups[1]),
                        build(level-1,x,y+half,groups[2]),build(level-1,x+half,y+half,groups[3]))
        }
        val root=evolve(build(level,0,0,positions),steps)
        val output=HashSet<Cell>()
        fun unpack(n:Node,x:Long,y:Long){
            if(n.population==0L)return
            if(n.level==0){
                val cx=x+ox+side/4;val cy=y+oy+side/4
                require(cx in Int.MIN_VALUE.toLong()..Int.MAX_VALUE.toLong() &&
                        cy in Int.MIN_VALUE.toLong()..Int.MAX_VALUE.toLong())
                output.add(Cell(cx.toInt(),cy.toInt()));return
            }
            val half=1L shl(n.level-1)
            unpack(n.nw!!,x,y);unpack(n.ne!!,x+half,y)
            unpack(n.sw!!,x,y+half);unpack(n.se!!,x+half,y+half)
        }
        unpack(root,0,0)
        return output
    }
}
