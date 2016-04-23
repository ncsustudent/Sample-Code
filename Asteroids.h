/* Alien Shot-Asteroid Collision  */
if(!alienshots.empty() && !asteroidCollide){
	boost::ptr_vector<Bullet>::iterator a = alienshots.begin();
	while(a != alienshots.end()){
		//Bound Check
		boundCheck(*a);
		//Collision Check
		if(pointInPolygon(*currAsteroid,a->getPosition().x,a->getPosition().y)){
			//If bullet collides, remove the bullet and trigger asteroid to destroy
			a = alienshots.erase(a);
			asteroidCollide = true;
			//Break since current asteroid iteration has terminated
			break;
		}else{
			++a;
		}
	}
}

/* Render Alien Ship */
void drawAlien(void){
	if(alien == NULL){ return; }
	glPushMatrix();	
	//Translate to current alien position
	glTranslated(alien->getPosition().x,alien->getPosition().y,0);
	//Scale model based on type
	if(alien->getType()){
		glScaled(ALSCALEL,ALSCALEL,0);
	}else{
		glScaled(ALSCALES,ALSCALES,0);
	}
	//Display static model of ship
	glBegin(GL_POLYGON);
	glVertex3f(4,-4,0);
	glVertex3f(16,0,0);
	glVertex3f(0,6,0);
	glVertex3f(-16,0,0);
	glVertex3f(-4,-4,0);
	glEnd();
	glPopMatrix();
}