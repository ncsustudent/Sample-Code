/* Table storing the set of memory-resident pages.  Multiple arrays storing the page in
 * each memory frame, which process owns that page and when that page was last accessed
 * by the process. 
 * E.g.
 * page   who    when last used
 *    1    A       3
 *    4    B       10
*/
typedef struct _resident {
    /* Array of page numbers, one for each memory frame recording which page is in it */
    int *page;

    /* Array of process names, recording which process owns the page in each frame. */
    char *who;

    /* Array of logical times (i.e., the count) when each frame was last accessed*/
    int *time;

    /* Total number of memory frames available (length of page, who and time arrays). */
    int size;

    /* Count of the number of accesses */
    int count;
}RESIDENT;

/* Page Table Access Function
*  In: p -  the specific process of argument pg
*      pg - the specific page number we want to access in page table
*  Out: 0 if found, 1 if missed, or -1 if error
*/
int resident_access(RESIDENT *pres, char proc, int page)
{
	//Size of RES
	int res_size = pres->size;
	
	for(int i = 0; i < res_size; i++){
		if(pres->who[i] == proc && pres->page[i] == page){
			//If the correct process and page number is found, update access time and return
			(pres->time[i])++;
			(pres->count)++;
			return 0;
		}
	}
	//Else page fault
	return 1;
}