//Get maximum number of processes
	if((fp = fopen("/proc/sys/kernel/pid_max","r")) == NULL){
		printf("Cannot open file!\n");
		exit(1);
	}
	
	fgets(str,LINE_MAX,fp);
	sscanf(str,"%ld",&NUM_P);
	fclose(fp);
	
	//Create array of processes
	procs = malloc(sizeof(PID) * NUM_P);
	
	//Change to /proc directory
	if((dirp = opendir("/proc")) == NULL){
		printf("Could not open directory!\n");
		exit(1);
	}
	
	//Initalize current struct
	PID curr = {.id=0,.name="/0",.state=0,.ppid=0,.mem=0};
	int _state, _ppid = 0;
	unsigned long int _id, _mem = 0;
	char fname[NAME_MAX];
	
	while((dirent = readdir(dirp)) != NULL){

		//Save name
		strcpy(pname,dirent->d_name);

		//Check directory name:
		//Only checks first char: if first char is not a digit, skip
		if(!isdigit(pname[0])){
			continue;
		}
		
		//Get file name
		*filen = NULL;
		strcat(filen,"/proc/");
		strcat(filen,pname);
		strcat(filen,"/stat");
		
		//Open /proc/[pid]/stat
		if((fp = fopen(filen,"r")) == NULL){
			printf("Cannot open PID file!\n");
			continue;
		}
		
		//Get process info
		fgets(str,LINE_MAX,fp);
		
		//...
	}