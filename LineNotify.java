	if(cond.get("line_use_yn").equals("Y")) {
			StringBuffer buffer = new StringBuffer();
			buffer.append(rule.get("event_nm"));
			buffer.append("["+event.get("field_val")+"]");
			
			alarm.put("sms_msg", buffer.toString());
			
			List<Map<String,Object>> tokenKEY = alarmDAO.selectEventLineToken(alarm);
			for(Map<String,Object> key:tokenKEY) {
				
				Object token = key.get("line_token");
				if(token instanceof String && !((String) token).isEmpty()) {
					logger.info("#####Event LINE_NOTIFY START !");
					int statusCode = -1;
					try {
						URL url = new URL(strEndpoint);
						HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

						connection.setRequestMethod("POST"); // 요청 방식 설정 (GET/POST 등)
						connection.addRequestProperty("Authorization", "Bearer " + token); // request Header 설정
																							// key-value 형식으로 다양한 요청
																							// 설정이 가능하다.
						connection.addRequestProperty("Content-Type", "application/x-www-form-urlencoded"); //

						connection.setConnectTimeout(15 * 1000); // 서버 연결 제한 시간
						connection.setDoOutput(true); // Server에서 온 데이터를 출력 할수 있는 상태인지 여부를 확인한다. ( default : true )
						connection.setUseCaches(false); // 캐시에 저장된 결과가 아닌 동적으로 그 순간에 생성된 결과를 읽도록
						connection.connect();

						connection.setReadTimeout(1000); // 서버 연결 후 데이터 read 제한 시간 // URL호출시 발생하는 무한 대기 상태에 빠지지 않도록
															// connectionTimeOut 발생시, ReadTimeOut 발생시 시간설정 꼭하기 !
						// connection.setDoInput( true ); // Server에서 온 데이터를 입력 받을 수 있는 상태인지 여부 확인 (
						// defalut : false )

						try (OutputStream os = connection.getOutputStream();
								BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"))) {

							writer.write("message=" + buffer.toString());
							writer.flush();
							// writer.close();
						} catch (Exception e) {
							throw e;
						}

						statusCode = connection.getResponseCode();
						
						if ( statusCode == 200 ) {
							alarmDAO.insertAlarmEventLINE(alarm);
						} else {
							throw new Exception( "Error:(StatusCode)" + statusCode + ", " + connection.getResponseMessage() );
						}
						connection.disconnect();
					} catch (Exception e) {
						e.printStackTrace();
						logger.info("#####Event LINE_NOTIFY ERROR !");
					}
				}
			}
		}
