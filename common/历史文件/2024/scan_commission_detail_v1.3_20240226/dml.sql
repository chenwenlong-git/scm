UPDATE process
SET process_type='INDEPENDENT_PROCESS'
WHERE del_timestamp = 0;
INSERT INTO process_composite (process_composite_id, parent_process_id, parent_process_code, sub_process_id,
                               sub_process_code, create_time, create_user, create_username, update_time, update_user,
                               update_username, del_timestamp, version)
VALUES (-- 拔毛-4*4+娃娃发-4*4
               FLOOR(RAND() * (2000000000000000000 - 1000000000000000000 + 1)) + 1000000000000000000,
               1623214274942091265, '0310', 1623214848148258817,-- 此处填写 sub_process_id，对应数据中的第三列
               '0316',-- 此处填写 sub_process_code，对应数据中的第二列
               CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', 0, 1),
       (-- 拔毛-4*4+娃娃发-4*4
               FLOOR(RAND() * (2000000000000000000 - 1000000000000000000 + 1)) + 1000000000000000000,
               1623214274942091265, '0310', 1623214914481176578,-- 此处填写 sub_process_id，对应数据中的第三列
               '0317',-- 此处填写 sub_process_code，对应数据中的第二列
               CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', 0, 1),
       (-- 拔毛-13*4+娃娃发-13*4
               FLOOR(RAND() * (2000000000000000000 - 1000000000000000000 + 1)) + 1000000000000000000,
               1623214341522472962, '0311', 1623214452860272641,-- 此处填写 sub_process_id，对应数据中的第三列
               '0313',-- 此处填写 sub_process_code，对应数据中的第二列
               CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', 0, 1),
       (-- 拔毛-13*4+娃娃发-13*4
               FLOOR(RAND() * (2000000000000000000 - 1000000000000000000 + 1)) + 1000000000000000000,
               1623214341522472962, '0311', 1623214517632909313,-- 此处填写 sub_process_id，对应数据中的第三列
               '0314',-- 此处填写 sub_process_code，对应数据中的第二列
               CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', 0, 1),
       (-- 染色-简单+漂扣-4*4
               FLOOR(RAND() * (2000000000000000000 - 1000000000000000000 + 1)) + 1000000000000000000,
               1623220832362491905, '0278', 1623217412793188354,-- 此处填写 sub_process_id，对应数据中的第三列
               '0240',-- 此处填写 sub_process_code，对应数据中的第二列
               CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', 0, 1),
       (-- 染色-简单+漂扣-4*4
               FLOOR(RAND() * (2000000000000000000 - 1000000000000000000 + 1)) + 1000000000000000000,
               1623220832362491905, '0278', 1623221647034740738,-- 此处填写 sub_process_id，对应数据中的第三列
               '0287',-- 此处填写 sub_process_code，对应数据中的第二列
               CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', 0, 1),
       (-- 染色-简单+漂扣-5*5
               FLOOR(RAND() * (2000000000000000000 - 1000000000000000000 + 1)) + 1000000000000000000,
               1699390011905982464, '0296', 1623217412793188354,-- 此处填写 sub_process_id，对应数据中的第三列
               '0240',-- 此处填写 sub_process_code，对应数据中的第二列
               CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', 0, 1),
       (-- 染色-简单+漂扣-5*5
               FLOOR(RAND() * (2000000000000000000 - 1000000000000000000 + 1)) + 1000000000000000000,
               1699390011905982464, '0296', 1623221598556975106,-- 此处填写 sub_process_id，对应数据中的第三列
               '0286',-- 此处填写 sub_process_code，对应数据中的第二列
               CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', 0, 1),
       (-- 染色-简单+漂扣-13*4
               FLOOR(RAND() * (2000000000000000000 - 1000000000000000000 + 1)) + 1000000000000000000,
               1699390087738949632, '0297', 1623217412793188354,-- 此处填写 sub_process_id，对应数据中的第三列
               '0240',-- 此处填写 sub_process_code，对应数据中的第二列
               CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', 0, 1),
       (-- 染色-简单+漂扣-13*4
               FLOOR(RAND() * (2000000000000000000 - 1000000000000000000 + 1)) + 1000000000000000000,
               1699390087738949632, '0297', 1623221541606715394,-- 此处填写 sub_process_id，对应数据中的第三列
               '0285',-- 此处填写 sub_process_code，对应数据中的第二列
               CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', 0, 1),
       (-- 染色-中等+漂扣-4*4
               FLOOR(RAND() * (2000000000000000000 - 1000000000000000000 + 1)) + 1000000000000000000,
               1699390173562847232, '0298', 1623217353850634242,-- 此处填写 sub_process_id，对应数据中的第三列
               '0239',-- 此处填写 sub_process_code，对应数据中的第二列
               CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', 0, 1),
       (-- 染色-中等+漂扣-4*4
               FLOOR(RAND() * (2000000000000000000 - 1000000000000000000 + 1)) + 1000000000000000000,
               1699390173562847232, '0298', 1623221647034740738,-- 此处填写 sub_process_id，对应数据中的第三列
               '0287',-- 此处填写 sub_process_code，对应数据中的第二列
               CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', 0, 1),
       (-- 染色-中等+漂扣-5*5
               FLOOR(RAND() * (2000000000000000000 - 1000000000000000000 + 1)) + 1000000000000000000,
               1699390228671807488, '0299', 1623217353850634242,-- 此处填写 sub_process_id，对应数据中的第三列
               '0239',-- 此处填写 sub_process_code，对应数据中的第二列
               CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', 0, 1),
       (-- 染色-中等+漂扣-5*5
               FLOOR(RAND() * (2000000000000000000 - 1000000000000000000 + 1)) + 1000000000000000000,
               1699390228671807488, '0299', 1623221598556975106,-- 此处填写 sub_process_id，对应数据中的第三列
               '0286',-- 此处填写 sub_process_code，对应数据中的第二列
               CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', 0, 1),
       (-- 染色-中等+漂扣-13*4
               FLOOR(RAND() * (2000000000000000000 - 1000000000000000000 + 1)) + 1000000000000000000,
               1699390276839194624, '02100', 1623217353850634242,-- 此处填写 sub_process_id，对应数据中的第三列
               '0239',-- 此处填写 sub_process_code，对应数据中的第二列
               CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', 0, 1),
       (-- 染色-中等+漂扣-13*4
               FLOOR(RAND() * (2000000000000000000 - 1000000000000000000 + 1)) + 1000000000000000000,
               1699390276839194624, '02100', 1623221541606715394,-- 此处填写 sub_process_id，对应数据中的第三列
               '0285',-- 此处填写 sub_process_code，对应数据中的第二列
               CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', 0, 1),
       (-- 染色-复杂+漂扣-4*4
               FLOOR(RAND() * (2000000000000000000 - 1000000000000000000 + 1)) + 1000000000000000000,
               1699390327774773248, '02101', 1623217286259425281,-- 此处填写 sub_process_id，对应数据中的第三列
               '0238',-- 此处填写 sub_process_code，对应数据中的第二列
               CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', 0, 1),
       (-- 染色-复杂+漂扣-4*4
               FLOOR(RAND() * (2000000000000000000 - 1000000000000000000 + 1)) + 1000000000000000000,
               1699390327774773248, '02101', 1623221647034740738,-- 此处填写 sub_process_id，对应数据中的第三列
               '0287',-- 此处填写 sub_process_code，对应数据中的第二列
               CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', 0, 1),
       (-- 染色-复杂+漂扣-5*5
               FLOOR(RAND() * (2000000000000000000 - 1000000000000000000 + 1)) + 1000000000000000000,
               1699390380706938880, '02102', 1623217286259425281,-- 此处填写 sub_process_id，对应数据中的第三列
               '0238',-- 此处填写 sub_process_code，对应数据中的第二列
               CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', 0, 1),
       (-- 染色-复杂+漂扣-5*5
               FLOOR(RAND() * (2000000000000000000 - 1000000000000000000 + 1)) + 1000000000000000000,
               1699390380706938880, '02102', 1623221598556975106,-- 此处填写 sub_process_id，对应数据中的第三列
               '0286',-- 此处填写 sub_process_code，对应数据中的第二列
               CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', 0, 1),
       (-- 染色-复杂+漂扣-13*4
               FLOOR(RAND() * (2000000000000000000 - 1000000000000000000 + 1)) + 1000000000000000000,
               1699390432082919424, '02103', 1623217286259425281,-- 此处填写 sub_process_id，对应数据中的第三列
               '0238',-- 此处填写 sub_process_code，对应数据中的第二列
               CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', 0, 1),
       (-- 染色-复杂+漂扣-13*4
               FLOOR(RAND() * (2000000000000000000 - 1000000000000000000 + 1)) + 1000000000000000000,
               1699390432082919424, '02103', 1623221541606715394,-- 此处填写 sub_process_id，对应数据中的第三列
               '0285',-- 此处填写 sub_process_code，对应数据中的第二列
               CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', 0, 1),
       (-- 染色-超级复杂+漂扣-4*4
               FLOOR(RAND() * (2000000000000000000 - 1000000000000000000 + 1)) + 1000000000000000000,
               1699390552602099712, '02104', 1623217233692213249,-- 此处填写 sub_process_id，对应数据中的第三列
               '0237',-- 此处填写 sub_process_code，对应数据中的第二列
               CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', 0, 1),
       (-- 染色-超级复杂+漂扣-4*4
               FLOOR(RAND() * (2000000000000000000 - 1000000000000000000 + 1)) + 1000000000000000000,
               1699390552602099712, '02104', 1623221647034740738,-- 此处填写 sub_process_id，对应数据中的第三列
               '0287',-- 此处填写 sub_process_code，对应数据中的第二列
               CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', 0, 1),
       (-- 染色-超级复杂+漂扣-5*5
               FLOOR(RAND() * (2000000000000000000 - 1000000000000000000 + 1)) + 1000000000000000000,
               1699390601675407360, '02105', 1623217233692213249,-- 此处填写 sub_process_id，对应数据中的第三列
               '0237',-- 此处填写 sub_process_code，对应数据中的第二列
               CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', 0, 1),
       (-- 染色-超级复杂+漂扣-5*5
               FLOOR(RAND() * (2000000000000000000 - 1000000000000000000 + 1)) + 1000000000000000000,
               1699390601675407360, '02105', 1623221598556975106,-- 此处填写 sub_process_id，对应数据中的第三列
               '0286',-- 此处填写 sub_process_code，对应数据中的第二列
               CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', 0, 1),
       (-- 染色-超级复杂+漂扣-13*4
               FLOOR(RAND() * (2000000000000000000 - 1000000000000000000 + 1)) + 1000000000000000000,
               1699390648249008128, '02106', 1623217233692213249,-- 此处填写 sub_process_id，对应数据中的第三列
               '0237',-- 此处填写 sub_process_code，对应数据中的第二列
               CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', 0, 1),
       (-- 染色-超级复杂+漂扣-13*4
               FLOOR(RAND() * (2000000000000000000 - 1000000000000000000 + 1)) + 1000000000000000000,
               1699390648249008128, '02106', 1623221541606715394,-- 此处填写 sub_process_id，对应数据中的第三列
               '0285',-- 此处填写 sub_process_code，对应数据中的第二列
               CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', 0, 1),
       (-- 拔毛-5*5+娃娃发-5*5
               FLOOR(RAND() * (2000000000000000000 - 1000000000000000000 + 1)) + 1000000000000000000,
               1700043940671094784, '0335', 1638224312068800512,-- 此处填写 sub_process_id，对应数据中的第三列
               '0327',-- 此处填写 sub_process_code，对应数据中的第二列
               CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', 0, 1),
       (-- 拔毛-5*5+娃娃发-5*5
               FLOOR(RAND() * (2000000000000000000 - 1000000000000000000 + 1)) + 1000000000000000000,
               1700043940671094784, '0335', 1638224533016346624,-- 此处填写 sub_process_id，对应数据中的第三列
               '0328',-- 此处填写 sub_process_code，对应数据中的第二列
               CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', 0, 1),
       (-- 染色-简单/复杂+漂扣4*4
               FLOOR(RAND() * (2000000000000000000 - 1000000000000000000 + 1)) + 1000000000000000000,
               1718196564037578752, '0103', 1691036171779493888,-- 此处填写 sub_process_id，对应数据中的第三列
               '0101',-- 此处填写 sub_process_code，对应数据中的第二列
               CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', 0, 1),
       (-- 染色-简单/复杂+漂扣4*4
               FLOOR(RAND() * (2000000000000000000 - 1000000000000000000 + 1)) + 1000000000000000000,
               1718196564037578752, '0103', 1623221647034740738,-- 此处填写 sub_process_id，对应数据中的第三列
               '0287',-- 此处填写 sub_process_code，对应数据中的第二列
               CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', 0, 1),
       (-- 染色-简单/复杂+漂扣5*5
               FLOOR(RAND() * (2000000000000000000 - 1000000000000000000 + 1)) + 1000000000000000000,
               1718196726730436608, '0104', 1691036171779493888,-- 此处填写 sub_process_id，对应数据中的第三列
               '0101',-- 此处填写 sub_process_code，对应数据中的第二列
               CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', 0, 1),
       (-- 染色-简单/复杂+漂扣5*5
               FLOOR(RAND() * (2000000000000000000 - 1000000000000000000 + 1)) + 1000000000000000000,
               1718196726730436608, '0104', 1623221598556975106,-- 此处填写 sub_process_id，对应数据中的第三列
               '0286',-- 此处填写 sub_process_code，对应数据中的第二列
               CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', 0, 1),
       (-- 染色-简单/复杂+漂扣13*4
               FLOOR(RAND() * (2000000000000000000 - 1000000000000000000 + 1)) + 1000000000000000000,
               1718197037436063744, '0105', 1691036171779493888,-- 此处填写 sub_process_id，对应数据中的第三列
               '0101',-- 此处填写 sub_process_code，对应数据中的第二列
               CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', 0, 1),
       (-- 染色-简单/复杂+漂扣13*4
               FLOOR(RAND() * (2000000000000000000 - 1000000000000000000 + 1)) + 1000000000000000000,
               1718197037436063744, '0105', 1623221541606715394,-- 此处填写 sub_process_id，对应数据中的第三列
               '0285',-- 此处填写 sub_process_code，对应数据中的第二列
               CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', 0, 1),
       (-- 染色—简单/超级复杂+漂扣4*4
               FLOOR(RAND() * (2000000000000000000 - 1000000000000000000 + 1)) + 1000000000000000000,
               1718197257985150976, '0106', 1714487927536439296,-- 此处填写 sub_process_id，对应数据中的第三列
               '02107',-- 此处填写 sub_process_code，对应数据中的第二列
               CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', 0, 1),
       (-- 染色—简单/超级复杂+漂扣4*4
               FLOOR(RAND() * (2000000000000000000 - 1000000000000000000 + 1)) + 1000000000000000000,
               1718197257985150976, '0106', 1623221647034740738,-- 此处填写 sub_process_id，对应数据中的第三列
               '0287',-- 此处填写 sub_process_code，对应数据中的第二列
               CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', 0, 1),
       (-- 染色—简单/超级复杂+漂扣5*5
               FLOOR(RAND() * (2000000000000000000 - 1000000000000000000 + 1)) + 1000000000000000000,
               1718197356479991808, '0107', 1714487927536439296,-- 此处填写 sub_process_id，对应数据中的第三列
               '02107',-- 此处填写 sub_process_code，对应数据中的第二列
               CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', 0, 1),
       (-- 染色—简单/超级复杂+漂扣5*5
               FLOOR(RAND() * (2000000000000000000 - 1000000000000000000 + 1)) + 1000000000000000000,
               1718197356479991808, '0107', 1623221598556975106,-- 此处填写 sub_process_id，对应数据中的第三列
               '0286',-- 此处填写 sub_process_code，对应数据中的第二列
               CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', 0, 1),
       (-- 染色—简单/超级复杂+漂扣*13*4
               FLOOR(RAND() * (2000000000000000000 - 1000000000000000000 + 1)) + 1000000000000000000,
               1718197483395436544, '0108', 1714487927536439296,-- 此处填写 sub_process_id，对应数据中的第三列
               '02107',-- 此处填写 sub_process_code，对应数据中的第二列
               CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', 0, 1),
       (-- 染色—简单/超级复杂+漂扣*13*4
               FLOOR(RAND() * (2000000000000000000 - 1000000000000000000 + 1)) + 1000000000000000000,
               1718197483395436544, '0108', 1623221541606715394,-- 此处填写 sub_process_id，对应数据中的第三列
               '0285',-- 此处填写 sub_process_code，对应数据中的第二列
               CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', 0, 1);-- 更新成组合工序
UPDATE process
SET process_type='COMPOUND_PROCESS',
    commission=0.00,
    extra_commission=0.00
WHERE process_code IN
      ('0310', '0311', '0278', '0296', '0297', '0298', '0299', '02100', '02101', '02102', '02103', '02104', '02105',
       '02106', '0335', '0103', '0104', '0105', '0106', '0107', '0108');