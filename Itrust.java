public void testRequestAppt() throws Exception{
		//Login as Random person
		WebConversation wc = login(randomS, password);
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Patient Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, randomP, 0L, "");
		wr = wr.getLinkWith("Request An Appointment").click();
		
		//Request an appt
		WebForm wf = wr.getFormWithID("mainForm");
		//Edit appt
		wf.setParameter("apptType", "General Checkup");
		wf.setParameter("schedDate", "10/08/2020");
		wf.setParameter("time1", "08");
		wf.setParameter("time2", "00");
		wf.setParameter("time3", "AM");
		wf.setParameter("hcp", currHCP);
		wf.setParameter("comment", "testRequestAppt");
		SubmitButton[] buttons = wf.getSubmitButtons();
		wr = wf.submit(buttons[0]);
		
		assertTrue(wr.getText().contains("Success"));
		
		//Delete SQL Data
		gen.UC22();
}

/**
 UC22 - RequestAppt: ADD Data 
INSERT INTO appointment(doctor_id,patient_id,sched_date,appt_type,comment)
VALUES('9000000003', '1','2020-10-08 08:00:00', 'General Checkup', 'HTTP Test 1');
*/