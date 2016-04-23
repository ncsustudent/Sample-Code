/* Snippet from producer/consumer assignment */

/** Place the given item in an available slot in the bounded buffer between the
    adjChooser and the nounChooser.  Block until a slot is available. */
void enqueueAdj( Item *it ) {
	pthread_mutex_lock(&mutex);
	
	/* Block buffer if full */
	if (num == BUFF_LIMIT){
		pthread_cond_wait(&prod, &mutex);
	}
	
	/* Add adj (address) to buffer */
	buffer_a[in] = it;
	in = (in + 1) % BUFF_LIMIT;
	num++;
	
	pthread_mutex_unlock(&mutex);
	pthread_cond_signal(&cons);
}

/** Return the next item from the bounded buffer btween the adjChooser
    and the nounChooser.  If the buffer is empty, block until there is
    something in it. */
Item *dequeueAdj() {
	pthread_mutex_lock(&mutex);
	
	/* Block buffer if empty */
	if (num == 0){
		pthread_cond_wait(&cons, &mutex);
	}
		
	/* Remove adj from buffer */
	Item *i;
	i = buffer_a[out];
	out = (out + 1) % BUFF_LIMIT;
	num--;
	
	pthread_mutex_unlock(&mutex);
	pthread_cond_signal(&prod);

	return i;
}