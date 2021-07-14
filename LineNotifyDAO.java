	public List<Map<String,Object>> selectEventLineToken(Map<String, Object> m) {
		SqlSession session = factory.openSession();
		List<Map<String,Object>> list = null;
		try {
			list = session.selectList("AlarmMapper.selectEventLineToken", m);			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return list;
	}
	

	public void insertAlarmEventLINE(Map<String, Object> m) {
	SqlSession session = factory.openSession();
	try {
		session.insert("AlarmMapper.insertAlarmEventLINE", m);
		session.commit();
	} finally {
		session.close();
	}
