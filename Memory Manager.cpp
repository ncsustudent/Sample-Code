  /* Snippet of custom memory manager */
  void* allocate(int aSize)
  { 
  
	//Get the free index
	int findex = *((int *) (MM_pool+MM_POOL_SIZE-4));
	
	//If the allocation size is less than the maximum block size (includes size of free index)
    if(aSize < MM_POOL_SIZE - findex - 4){
		
		//Store size of object
		*((int *) (MM_pool+findex)) = aSize;
		
		//Shift the free index (including size block)
		*((int *) (MM_pool+MM_POOL_SIZE-4)) += (aSize + 4);
		
		//Create pointer of allocation block
		void * ptr;
		ptr = MM_pool + findex + 4; //NOTE: it points after size block
		return ptr;
		
	} else {
		onOutOfMemory();
		return ((void*) 0);
	}
		
  }