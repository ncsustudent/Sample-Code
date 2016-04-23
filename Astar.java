public class Astar {
	/**
	 * Executes A* Search algorithm
	 * Path is determined via Vertex object
	 * Explored set shows all expanded vertices
	 * @param graph
	 * @param start vertex
	 * @param end vertex
	 */
	public static void search(Graph map, String start, String end) {
		/* Setup */
		VertexComparator comparator = new VertexComparator();						//Comparator
		PriorityQueue<Vertex> queue = new PriorityQueue<Vertex>(112, comparator);	//Priority Queue
		HashSet<Vertex> explored = new HashSet<Vertex>();							//Set of explored nodes
		HashSet<Vertex> expanded = new HashSet<Vertex>();							//Set of expanded nodes
		Vertex root = null;
		Vertex goal = null;
		
		//Assign root and goal
		for(Vertex curr : map.vertices()){
			if(curr.getState().equalsIgnoreCase(start)){
				root = curr;
			} else if(curr.getState().equalsIgnoreCase(end)){
				goal = curr;
			}
		}
		
		//Check if erroneous
		if(root == null || goal == null){
			System.out.println("The start and end arguments are invalid");
			System.exit(-1);
		}
		
		//Otherwise, add the root node
		root.setParent(new Vertex("root"));
		queue.add(root);
		explored.add(root);
		expanded.add(root);
		
		//Algorithm Setup
		Vertex i = null;			 												//Current Vertex
		//Vertex p = null; 			 												//Prev Vertex
		boolean hasSuccess = false;  												//True if goal has been found
		boolean isExplored = false;  												//True if vertex has been expanded
		int path = 0;																//Path length
		
		//Main loop
		while(!queue.isEmpty()){		
			//Get the current, minimum vertex
			i = queue.poll();
			//Add to expansion
			expanded.add(i);
			
			//System.out.println("Current vertex: " + i.getState());
			
			//Check if matches goal
			if(i.getState().equals(goal.getState())){
				hasSuccess = true;
				break;
			}else{	
				//Otherwise expand node for all connected nodes
				for(Edge e : i.incidentEdges()){
					
					//Get specific edge's connected nodes
					Vertex child = e.getOppositeVertex(i);
					
					//Check if previously explored
					isExplored = false;
					for(Vertex ex : explored){
						if(ex.getState().equals(child.getState())){
							isExplored = true;
							break;
						}
					}
					
					if(!isExplored){						
						//Assign path's edge value
						child.setEdgeValue(e.getWeight());
						//Set up path
						child.setParent(i);
						//Estimate its distance AND sum of path
						path = i.getPathLength() + child.getEdgeValue();
						child.setHPathValue(goal,path);
						//Add to priority queue
						queue.add(child);
						//Insert into explored set
						explored.add(child);
						
						//System.out.println("Added vertex with hat and edge: " + child.getState() + ", " + child.getEvalValue() + ", " + child.getEdgeValue());

					}
				}
			}//end else
		}//end while
			
		if(hasSuccess){
			System.out.println(i.getExpansionList(expanded));		//List of expanded nodes
			System.out.println(i.getPathAsString(root.getState(),goal.getState())); //List of correct path
		}else{
			System.out.println("Error!  No path found.");
		}
		
	}

}
