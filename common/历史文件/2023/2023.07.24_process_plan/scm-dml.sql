update process_order_scan
set processing_time=receipt_time,
    processing_user= receipt_username,
    processing_username=receipt_username
where del_timestamp = 0;

update process_order
set need_process_plan='FALSE',
    need_process_plan='FALSE',
    process_plan_delay='FALSE'
where del_timestamp = 0;


-- 新增职级数据
insert into employee_grade(employee_grade_id, grade_type, grade_name, grade_level, create_time, create_user,
                           update_time, update_user, del_timestamp, version)
values (1698304759049409025, 'STYLIST', '高级造型师', 9.0, current_timestamp, 'SYSTEM', current_timestamp, 'SYSTEM', 0,
        1),
       (1698304759049409026, 'STYLIST', '中级造型师', 8.0, current_timestamp, 'SYSTEM', current_timestamp, 'SYSTEM', 0,
        1),
       (1698304759049409027, 'STYLIST', '初级造型师', 7.0, current_timestamp, 'SYSTEM', current_timestamp, 'SYSTEM', 0,
        1),

       (1698304759049409028, 'COLORIST', '高级染发师', 6.0, current_timestamp, 'SYSTEM', current_timestamp, 'SYSTEM', 0,
        1),
       (1698304759049409029, 'COLORIST', '中级染发师', 5.0, current_timestamp, 'SYSTEM', current_timestamp, 'SYSTEM', 0,
        1),
       (1698304759049409030, 'COLORIST', '初级染发师', 4.0, current_timestamp, 'SYSTEM', current_timestamp, 'SYSTEM', 0,
        1),

       (1698304759049409031, 'CLIPS', '中级缝卡子', 2.0, current_timestamp, 'SYSTEM', current_timestamp, 'SYSTEM', 0,
        1),
       (1698304759049409032, 'CLIPS', '低级缝卡子', 1.0, current_timestamp, 'SYSTEM', current_timestamp, 'SYSTEM', 0,
        1),

       (1698304759049409033, 'HEADGEAR', '中级缝头套', 2.0, current_timestamp, 'SYSTEM', current_timestamp, 'SYSTEM', 0,
        1),
       (1698304759049409034, 'HEADGEAR', '低级缝头套', 1.0, current_timestamp, 'SYSTEM', current_timestamp, 'SYSTEM', 0,
        1);

-- 绑定员工职级关系
-- 高级造型师 (employee_grade_id: 1698304759049409025)
INSERT INTO employee_grade_relation(employee_grade_relation_id, employee_no, employee_name, employee_grade_id,
                                    create_time,
                                    create_user, update_time, update_user, del_timestamp, version)
VALUES (1698304759049409035, 'U489512', '郭羿凌', 1698304759049409025, current_timestamp, 'SYSTEM', current_timestamp,
        'SYSTEM', 0, 1),
       (1698304759049409036, 'U096296', '李刘杰', 1698304759049409025, current_timestamp, 'SYSTEM', current_timestamp,
        'SYSTEM', 0, 1),
       (1698304759049409037, 'U358440', '高昌锋', 1698304759049409025, current_timestamp, 'SYSTEM', current_timestamp,
        'SYSTEM', 0, 1),
       (1698304759049409038, 'U342056', '唐运龙', 1698304759049409025, current_timestamp, 'SYSTEM', current_timestamp,
        'SYSTEM', 0, 1),
       (1698304759049409039, 'U163880', '董华', 1698304759049409025, current_timestamp, 'SYSTEM', current_timestamp,
        'SYSTEM', 0, 1);


-- 中级造型师 (employee_grade_id: 1698304759049409026)
INSERT INTO employee_grade_relation(employee_grade_relation_id, employee_no, employee_name, employee_grade_id,
                                    create_time, create_user, update_time, update_user, del_timestamp, version)
VALUES (1698304759049409040, 'U011304', '简铨', 1698304759049409026, current_timestamp, 'SYSTEM', current_timestamp,
        'SYSTEM', 0, 1),
       (1698304759049409041, 'U207912', '杨鹏', 1698304759049409026, current_timestamp, 'SYSTEM', current_timestamp,
        'SYSTEM', 0, 1),
       (1698304759049409042, 'U515112', '王浩祺', 1698304759049409026, current_timestamp, 'SYSTEM', current_timestamp,
        'SYSTEM', 0, 1),
       (1698304759049409043, 'U227368', '熊豪', 1698304759049409026, current_timestamp, 'SYSTEM', current_timestamp,
        'SYSTEM', 0, 1),
       (1698304759049409044, 'U178216', '劳玉锋', 1698304759049409026, current_timestamp, 'SYSTEM', current_timestamp,
        'SYSTEM', 0, 1),
       (1698304759049409045, 'U047144', '黄斌英', 1698304759049409026, current_timestamp, 'SYSTEM', current_timestamp,
        'SYSTEM', 0, 1),
       (1698304759049409046, 'U309288', '陈泓树', 1698304759049409026, current_timestamp, 'SYSTEM', current_timestamp,
        'SYSTEM', 0, 1),
       (1698304759049409047, 'U243752', '耿晓阳', 1698304759049409026, current_timestamp, 'SYSTEM', current_timestamp,
        'SYSTEM', 0, 1),
       (1698304759049409048, 'U505896', '王涛', 1698304759049409026, current_timestamp, 'SYSTEM', current_timestamp,
        'SYSTEM', 0, 1),
       (1698304759049409049, 'U407592', '彭奕仲', 1698304759049409026, current_timestamp, 'SYSTEM', current_timestamp,
        'SYSTEM', 0, 1),
       (1698304759049409050, 'U131112', '张记生', 1698304759049409026, current_timestamp, 'SYSTEM', current_timestamp,
        'SYSTEM', 0, 1),
       (1698304759049409051, 'U344104', '段建伦', 1698304759049409026, current_timestamp, 'SYSTEM', current_timestamp,
        'SYSTEM', 0, 1);

-- 初级造型师 (employee_grade_id: 1698304759049409027)

-- 高级染发师 (employee_grade_id: 1698304759049409028)
INSERT INTO employee_grade_relation(employee_grade_relation_id, employee_no, employee_name, employee_grade_id,
                                    create_time, create_user, update_time, update_user, del_timestamp, version)
VALUES (1698304759049409075, 'U258856', '黄武', 1698304759049409028, current_timestamp, 'SYSTEM', current_timestamp,
        'SYSTEM', 0, 1),
       (1698304759049409076, 'U431144', '郑芳良', 1698304759049409028, current_timestamp, 'SYSTEM', current_timestamp,
        'SYSTEM', 0, 1),
       (1698304759049409077, 'U374824', '屈琳博', 1698304759049409028, current_timestamp, 'SYSTEM', current_timestamp,
        'SYSTEM', 0, 1),
       (1698304759049409078, 'U213032', '梁朝景', 1698304759049409028, current_timestamp, 'SYSTEM', current_timestamp,
        'SYSTEM', 0, 1),

       (1698304759049409052, 'U475176', '梁朝强', 1698304759049409028, current_timestamp, 'SYSTEM', current_timestamp,
        'SYSTEM', 0, 1),
       (1698304759049409053, 'U016424', '项永畅', 1698304759049409028, current_timestamp, 'SYSTEM', current_timestamp,
        'SYSTEM', 0, 1);


-- 中级染发师 (employee_grade_id: 1698304759049409029)
INSERT INTO employee_grade_relation(employee_grade_relation_id, employee_no, employee_name, employee_grade_id,
                                    create_time, create_user, update_time, update_user, del_timestamp, version)
VALUES (1698304759049409054, 'U406312', '王金海', 1698304759049409029, current_timestamp, 'SYSTEM', current_timestamp,
        'SYSTEM', 0, 1),
       (1698304759049409055, 'U273448', '蔡景辉', 1698304759049409029, current_timestamp, 'SYSTEM', current_timestamp,
        'SYSTEM', 0, 1),
       (1698304759049409056, 'U121896', '陈邹', 1698304759049409029, current_timestamp, 'SYSTEM', current_timestamp,
        'SYSTEM', 0, 1),
       (1698304759049409057, 'U292904', '薛淘淘', 1698304759049409029, current_timestamp, 'SYSTEM', current_timestamp,
        'SYSTEM', 0, 1),
       (1698304759049409058, 'U440360', '苏宝鹏', 1698304759049409029, current_timestamp, 'SYSTEM', current_timestamp,
        'SYSTEM', 0, 1),
       (1698304759049409059, 'U112680', '林王算', 1698304759049409029, current_timestamp, 'SYSTEM', current_timestamp,
        'SYSTEM', 0, 1),
       (1698304759049409060, 'U210984', '林王志', 1698304759049409029, current_timestamp, 'SYSTEM', current_timestamp,
        'SYSTEM', 0, 1),
       (1698304759049409061, 'U180264', '黄火飞', 1698304759049409029, current_timestamp, 'SYSTEM', current_timestamp,
        'SYSTEM', 0, 1),
       (1698304759049409062, 'U147496', '李东超', 1698304759049409029, current_timestamp, 'SYSTEM', current_timestamp,
        'SYSTEM', 0, 1),
       (1698304759049409063, 'U278568', '张云凡', 1698304759049409029, current_timestamp, 'SYSTEM', current_timestamp,
        'SYSTEM', 0, 1);


-- 初级染发师 (employee_grade_id: 1698304759049409030)
INSERT INTO employee_grade_relation(employee_grade_relation_id, employee_no, employee_name, employee_grade_id,
                                    create_time, create_user, update_time, update_user, del_timestamp, version)
VALUES (1698304759049409064, 'U324392', '巫文生', 1698304759049409030, current_timestamp, 'SYSTEM', current_timestamp,
        'SYSTEM', 0, 1),
       (1698304759049409065, 'U488232', '范军涌', 1698304759049409030, current_timestamp, 'SYSTEM', current_timestamp,
        'SYSTEM', 0, 1);


-- 中级缝卡子 (employee_grade_id: 1698304759049409031)
INSERT INTO employee_grade_relation(employee_grade_relation_id, employee_no, employee_name, employee_grade_id,
                                    create_time, create_user, update_time, update_user, del_timestamp, version)
VALUES (1698304759049409066, 'U032808', '李秀春', 1698304759049409031, current_timestamp, 'SYSTEM', current_timestamp,
        'SYSTEM', 0, 1),
       (1698304759049409067, 'U426024', '刘恒秀', 1698304759049409031, current_timestamp, 'SYSTEM', current_timestamp,
        'SYSTEM', 0, 1),
       (1698304759049409068, 'U393256', '余雪花', 1698304759049409031, current_timestamp, 'SYSTEM', current_timestamp,
        'SYSTEM', 0, 1);

-- 中级缝头套 (employee_grade_id: 1698304759049409033)
INSERT INTO employee_grade_relation(employee_grade_relation_id, employee_no, employee_name, employee_grade_id,
                                    create_time, create_user, update_time, update_user, del_timestamp, version)
VALUES (1698304759049409069, 'U507944', '刘燕', 1698304759049409033, current_timestamp, 'SYSTEM', current_timestamp,
        'SYSTEM', 0, 1),
       (1698304759049409070, 'U229416', '张艳', 1698304759049409033, current_timestamp, 'SYSTEM', current_timestamp,
        'SYSTEM', 0, 1),
       (1698304759049409071, 'U294952', '李梅霞', 1698304759049409033, current_timestamp, 'SYSTEM', current_timestamp,
        'SYSTEM', 0, 1),
       (1698304759049409072, 'U360488', '钱勋伟', 1698304759049409033, current_timestamp, 'SYSTEM', current_timestamp,
        'SYSTEM', 0, 1);

-- 低级缝头套 (employee_grade_id: 1698304759049409034)
INSERT INTO employee_grade_relation(employee_grade_relation_id, employee_no, employee_name, employee_grade_id,
                                    create_time, create_user, update_time, update_user, del_timestamp, version)
VALUES (1698304759049409073, 'U098344', '李平', 1698304759049409034, current_timestamp, 'SYSTEM', current_timestamp,
        'SYSTEM', 0, 1),
       (1698304759049409074, 'U079912', '马凤云', 1698304759049409034, current_timestamp, 'SYSTEM', current_timestamp,
        'SYSTEM', 0, 1);



-- 更新工序信息
-- 13*4头套
update process
set complex_coefficient=1,
    commission=6,
    setup_duration=0
where process_id = 1623219361197780994;
-- 13*6头套
update process
set complex_coefficient=1,
    commission=6,
    setup_duration=0
where process_id = 1623219041696673793;
-- 2*6头套
update process
set complex_coefficient=1,
    commission=6,
    setup_duration=0
where process_id = 1623218927167619073;
-- 4*4头套
update process
set complex_coefficient=1,
    commission=6,
    setup_duration=0
where process_id = 1638236227889332224;
-- 4个梳卡+松紧带
update process
set complex_coefficient=2,
    commission=1.5,
    setup_duration=0
where process_id = 1623218713312641025;
-- 5*5头套
update process
set complex_coefficient=1,
    commission=6,
    setup_duration=0
where process_id = 1623219466101518338;
-- 6*6头套
update process
set complex_coefficient=1,
    commission=6,
    setup_duration=0
where process_id = 1623219415312691201;
-- U型头套
update process
set complex_coefficient=1,
    commission=6,
    setup_duration=0
where process_id = 1623219309985329153;
-- V-part头套
update process
set complex_coefficient=1,
    commission=6,
    setup_duration=0
where process_id = 1623218876680781825;
-- 修剪蕾丝 13*4头套
update process
set complex_coefficient=1,
    commission=2,
    setup_duration=0
where process_id = 1638225825164591104;
-- 修剪蕾丝 13*6头套
update process
set complex_coefficient=1,
    commission=2,
    setup_duration=0
where process_id = 1638225915786100736;
-- 修剪蕾丝 4*4头套
update process
set complex_coefficient=1,
    commission=1,
    setup_duration=0
where process_id = 1623216017885118465;
-- 修剪蕾丝 44FR头套
update process
set complex_coefficient=1,
    commission=2,
    setup_duration=0
where process_id = 1623215960075026433;
-- 修剪蕾丝 5*5头套
update process
set complex_coefficient=1,
    commission=1,
    setup_duration=0
where process_id = 1638226118358401024;
-- 冰丝发带头套
update process
set complex_coefficient=1,
    commission=6,
    setup_duration=0
where process_id = 1623219095195021313;
-- 刘海片
update process
set complex_coefficient=1,
    commission=3,
    setup_duration=0
where process_id = 1623216714592567297;
-- 半圆马尾
update process
set complex_coefficient=1,
    commission=3,
    setup_duration=0
where process_id = 1623216661186494466;
-- 半头套
update process
set complex_coefficient=1,
    commission=6,
    setup_duration=0
where process_id = 1623218823031439361;
-- 卡子发
update process
set complex_coefficient=4,
    commission=10,
    setup_duration=0
where process_id = 1623216547617325058;
-- 发缝头套
update process
set complex_coefficient=1,
    commission=6,
    setup_duration=0
where process_id = 1623218771806404609;
-- 只缝松紧带
update process
set complex_coefficient=1,
    commission=0.5,
    setup_duration=0
where process_id = 1623218483590610945;
-- 娃娃发-13*4
update process
set complex_coefficient=2,
    commission=5,
    setup_duration=0
where process_id = 1623214517632909313;
-- 娃娃发-4*4
update process
set complex_coefficient=1,
    commission=2,
    setup_duration=0
where process_id = 1623214914481176578;
-- 娃娃发-5*5
update process
set complex_coefficient=1,
    commission=2,
    setup_duration=0
where process_id = 1638224533016346624;
-- 抽绳马尾
update process
set complex_coefficient=1,
    commission=3,
    setup_duration=0
where process_id = 1623216603472871426;
-- 拆/缝松紧带
update process
set complex_coefficient=1,
    commission=1,
    setup_duration=0
where process_id = 1623216294562381826;
-- 拔毛+娃娃发-13*4
update process
set complex_coefficient=2,
    commission=12,
    setup_duration=0
where process_id = 1623214341522472962;
-- 拔毛+娃娃发-4*4
update process
set complex_coefficient=1,
    commission=7,
    setup_duration=0
where process_id = 1623214274942091265;
-- 拔毛+娃娃发-5*5
update process
set complex_coefficient=1,
    commission=7,
    setup_duration=0
where process_id = 1700043940671094784;
-- 拔毛-13*4
update process
set complex_coefficient=2,
    commission=7,
    setup_duration=0
where process_id = 1623214452860272641;
-- 拔毛-4*4
update process
set complex_coefficient=1,
    commission=5,
    setup_duration=0
where process_id = 1623214848148258817;
-- 拔毛-5*5
update process
set complex_coefficient=1,
    commission=5,
    setup_duration=0
where process_id = 1638224312068800512;
-- 染色-中等
update process
set complex_coefficient=2,
    commission=10,
    setup_duration=1440
where process_id = 1623217353850634242;
-- 染色-中等/漂扣-13*4
update process
set complex_coefficient=2,
    commission=20,
    setup_duration=1440
where process_id = 1699390276839194624;
-- 染色-中等/漂扣-4*4
update process
set complex_coefficient=2,
    commission=15,
    setup_duration=1440
where process_id = 1699390173562847232;
-- 染色-中等/漂扣-5*5
update process
set complex_coefficient=2,
    commission=15,
    setup_duration=1440
where process_id = 1699390228671807488;
-- 染色-发帘发块（复杂）
update process
set complex_coefficient=2,
    commission=5,
    setup_duration=0
where process_id = 1623217123927277569;
-- 染色-发帘发块（简单）
update process
set complex_coefficient=1,
    commission=3,
    setup_duration=0
where process_id = 1623217172354711554;
-- 染色-复杂
update process
set complex_coefficient=4,
    commission=20,
    setup_duration=1440
where process_id = 1623217286259425281;
-- 染色-复杂/漂扣-13*4
update process
set complex_coefficient=3,
    commission=30,
    setup_duration=1440
where process_id = 1699390432082919424;
-- 染色-复杂/漂扣-4*4
update process
set complex_coefficient=3,
    commission=25,
    setup_duration=1440
where process_id = 1699390327774773248;
-- 染色-复杂/漂扣-5*5
update process
set complex_coefficient=3,
    commission=25,
    setup_duration=1440
where process_id = 1699390380706938880;
-- 染色-简单
update process
set complex_coefficient=1,
    commission=6,
    setup_duration=1440
where process_id = 1623217412793188354;
-- 染色-简单/复杂
update process
set complex_coefficient=3,
    commission=15,
    setup_duration=1440
where process_id = 1691036171779493888;
-- 染色-简单/漂扣-13*4
update process
set complex_coefficient=1,
    commission=16,
    setup_duration=1440
where process_id = 1699390087738949632;
-- 染色-简单/漂扣-4*4
update process
set complex_coefficient=1,
    commission=11,
    setup_duration=1440
where process_id = 1623220832362491905;
-- 染色-简单/漂扣-5*5
update process
set complex_coefficient=1,
    commission=11,
    setup_duration=1440
where process_id = 1699390011905982464;
-- 染色-超级复杂
update process
set complex_coefficient=5,
    commission=40,
    setup_duration=0
where process_id = 1623217233692213249;
-- 染色-超级复杂/漂扣-13*4
update process
set complex_coefficient=4,
    commission=50,
    setup_duration=1440
where process_id = 1699390648249008128;
-- 染色-超级复杂/漂扣-4*4
update process
set complex_coefficient=4,
    commission=45,
    setup_duration=1440
where process_id = 1699390552602099712;
-- 染色-超级复杂/漂扣-5*5
update process
set complex_coefficient=4,
    commission=45,
    setup_duration=1440
where process_id = 1699390601675407360;
-- 清洗-复杂
update process
set complex_coefficient=2,
    commission=5,
    setup_duration=0
where process_id = 1623216932469882882;
-- 清洗-简单
update process
set complex_coefficient=1,
    commission=3,
    setup_duration=0
where process_id = 1623217066020716545;
-- 漂扣-13*4
update process
set complex_coefficient=2,
    commission=10,
    setup_duration=300
where process_id = 1623221541606715394;
-- 漂扣-4*4
update process
set complex_coefficient=1,
    commission=5,
    setup_duration=0
where process_id = 1623221647034740738;
-- 漂扣-5*5
update process
set complex_coefficient=1,
    commission=5,
    setup_duration=300
where process_id = 1623220770664280066;
-- 离子烫-发帘发块
update process
set complex_coefficient=1,
    commission=5,
    setup_duration=0
where process_id = 1623217516585435137;
-- 离子烫-头套
update process
set complex_coefficient=2,
    commission=10,
    setup_duration=0
where process_id = 1623217462604742657;
-- 纯机制头套
update process
set complex_coefficient=1,
    commission=6,
    setup_duration=0
where process_id = 1623219146365530113;
-- 缝U-part卡子（4个九齿卡）
update process
set complex_coefficient=2,
    commission=1.5,
    setup_duration=0
where process_id = 1623217775017476097;
-- 缝Vpart卡子+松紧带
update process
set complex_coefficient=3,
    commission=2.5,
    setup_duration=0
where process_id = 1623217928487059457;
-- 缝半头套卡子
update process
set complex_coefficient=3,
    commission=2,
    setup_duration=0
where process_id = 1623217883410874369;
-- 缝马尾卡子
update process
set complex_coefficient=1,
    commission=0.5,
    setup_duration=0
where process_id = 1623218415856795650;
-- 返修加工-中等造型
update process
set complex_coefficient=1,
    commission=5,
    setup_duration=0
where process_id = 1623213850897956866;
-- 返修加工-复杂造型
update process
set complex_coefficient=2,
    commission=7,
    setup_duration=0
where process_id = 1623213766680526850;
-- 返修加工-简单造型
update process
set complex_coefficient=1,
    commission=2,
    setup_duration=0
where process_id = 1623213919458050050;
-- 造型-中等
update process
set complex_coefficient=2,
    commission=8,
    setup_duration=0
where process_id = 1623215101173841922;
-- 造型-复杂
update process
set complex_coefficient=3,
    commission=14,
    setup_duration=0
where process_id = 1623215042914959362;
-- 造型-简单
update process
set complex_coefficient=1,
    commission=3,
    setup_duration=0
where process_id = 1623215155074842625;
-- 造型-超级复杂
update process
set complex_coefficient=4,
    commission=20,
    setup_duration=0
where process_id = 1623214120398766081;
-- 镂空360头套
update process
set complex_coefficient=2,
    commission=10,
    setup_duration=0
where process_id = 1623218981508411393;

-- 绑定职级和工序信息
insert into employee_grade_process(employee_grade_process_id, employee_grade_id,
                                   process_id, process_name, process_num,
                                   create_time, create_user, update_time, update_user,
                                   del_timestamp, version)
values
-- 中级染发师
(1700078388867371008, 1698304759049409029, 1623217412793188354, '染色-简单', 30, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 中级染发师
(1700078388867371009, 1698304759049409029, 1623217353850634242, '染色-中等', 18, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 中级染发师
(1700078388867371010, 1698304759049409029, 1691036171779493888, '染色-简单/复杂', 15, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 中级染发师
(1700078388867371011, 1698304759049409029, 1623217286259425281, '染色-复杂', 12, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 中级染发师
(1700078388867371012, 1698304759049409029, 1623217172354711554, '染色-发帘发块（简单）', 25, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 中级染发师
(1700078388867371013, 1698304759049409029, 1623217123927277569, '染色-发帘发块（复杂）', 18, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 中级染发师
(1700078388867371014, 1698304759049409029, 1623217516585435137, '离子烫-发帘发块', 50, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 中级染发师
(1700078388871565312, 1698304759049409029, 1623217462604742657, '离子烫-头套', 25, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 中级染发师
(1700078388871565313, 1698304759049409029, 1623217066020716545, '清洗-简单', 80, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 中级染发师
(1700078388871565314, 1698304759049409029, 1623216932469882882, '清洗-复杂', 25, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 中级染发师
(1700078388871565315, 1698304759049409029, 1623221647034740738, '漂扣-4*4', 50, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 中级染发师
(1700078388871565316, 1698304759049409029, 1623220770664280066, '漂扣-5*5', 50, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 中级染发师
(1700078388871565317, 1698304759049409029, 1623221541606715394, '漂扣-13*4', 35, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 中级缝卡子
(1700078388871565318, 1698304759049409031, 1623218483590610945, '只缝松紧带', 150, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 中级缝卡子
(1700078388875759616, 1698304759049409031, 1623218415856795650, '缝马尾卡子', 150, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 中级缝卡子
(1700078388875759617, 1698304759049409031, 1623216294562381826, '拆/缝松紧带', 150, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 中级缝卡子
(1700078388875759618, 1698304759049409031, 1623218713312641025, '4个梳卡+松紧带', 90, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 中级缝卡子
(1700078388875759619, 1698304759049409031, 1623217775017476097, '缝U-part卡子（4个九齿卡）', 90, current_timestamp,
 'SYSTEM', current_timestamp, 'SYSTEM', 0, 0),
-- 中级缝卡子
(1700078388875759620, 1698304759049409031, 1623217883410874369, '缝半头套卡子', 60, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 中级缝卡子
(1700078388875759621, 1698304759049409031, 1623217928487059457, '缝Vpart卡子+松紧带', 60, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 中级缝卡子
(1700078388875759622, 1698304759049409031, 1623216547617325058, '卡子发', 9, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 中级缝卡子
(1700078388875759623, 1698304759049409031, 1623216017885118465, '修剪蕾丝 4*4头套', 400, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 中级缝卡子
(1700078388875759624, 1698304759049409031, 1638226118358401024, '修剪蕾丝 5*5头套', 400, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 中级缝卡子
(1700078388875759625, 1698304759049409031, 1638225825164591104, '修剪蕾丝 13*4头套', 300, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 中级缝卡子
(1700078388875759626, 1698304759049409031, 1623215960075026433, '修剪蕾丝 44FR头套', 300, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 中级缝卡子
(1700078388879953920, 1698304759049409031, 1638225915786100736, '修剪蕾丝 13*6头套', 300, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 中级缝头套
(1700078388879953921, 1698304759049409033, 1623216714592567297, '刘海片', 100, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 中级缝头套
(1700078388879953922, 1698304759049409033, 1623216603472871426, '抽绳马尾', 75, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 中级缝头套
(1700078388879953923, 1698304759049409033, 1623216661186494466, '半圆马尾', 75, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 中级缝头套
(1700078388879953924, 1698304759049409033, 1623218927167619073, '2*6头套', 35, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 中级缝头套
(1700078388879953925, 1698304759049409033, 1638236227889332224, '4*4头套', 30, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 中级缝头套
(1700078388879953926, 1698304759049409033, 1623219466101518338, '5*5头套', 30, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 中级缝头套
(1700078388879953927, 1698304759049409033, 1623219415312691201, '6*6头套', 30, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 中级缝头套
(1700078388879953928, 1698304759049409033, 1623219361197780994, '13*4头套', 30, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 中级缝头套
(1700078388879953929, 1698304759049409033, 1623219041696673793, '13*6头套', 30, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 中级缝头套
(1700078388879953930, 1698304759049409033, 1623219309985329153, 'U型头套', 35, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 中级缝头套
(1700078388879953931, 1698304759049409033, 1623219146365530113, '纯机制头套', 26, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 中级缝头套
(1700078388879953932, 1698304759049409033, 1623219095195021313, '冰丝发带头套', 26, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 中级缝头套
(1700078388879953933, 1698304759049409033, 1623218876680781825, 'V-part头套', 26, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 中级缝头套
(1700078388879953934, 1698304759049409033, 1623218823031439361, '半头套', 30, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 中级缝头套
(1700078388879953935, 1698304759049409033, 1623218771806404609, '发缝头套', 30, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 中级缝头套
(1700078388879953936, 1698304759049409033, 1623218981508411393, '镂空360头套', 22, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 中级缝头套
(1700078388879953937, 1698304759049409033, 1623216017885118465, '修剪蕾丝 4*4头套', 400, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 中级缝头套
(1700078388884148224, 1698304759049409033, 1638226118358401024, '修剪蕾丝 5*5头套', 400, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 中级缝头套
(1700078388884148225, 1698304759049409033, 1638225825164591104, '修剪蕾丝 13*4头套', 300, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 中级缝头套
(1700078388884148226, 1698304759049409033, 1623215960075026433, '修剪蕾丝 44FR头套', 300, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 中级缝头套
(1700078388884148227, 1698304759049409033, 1638225915786100736, '修剪蕾丝 13*6头套', 300, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 中级造型师
(1700078388884148228, 1698304759049409026, 1623215155074842625, '造型-简单', 40, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 中级造型师
(1700078388884148229, 1698304759049409026, 1623215101173841922, '造型-中等', 30, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 中级造型师
(1700078388884148230, 1698304759049409026, 1623215042914959362, '造型-复杂', 15, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 中级造型师
(1700078388884148231, 1698304759049409026, 1623214120398766081, '造型-超级复杂', 12, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 中级造型师
(1700078388884148232, 1698304759049409026, 1623214914481176578, '娃娃发-4*4', 60, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 中级造型师
(1700078388884148233, 1698304759049409026, 1638224533016346624, '娃娃发-5*5', 60, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 中级造型师
(1700078388884148234, 1698304759049409026, 1623214848148258817, '拔毛-4*4', 45, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 中级造型师
(1700078388884148235, 1698304759049409026, 1638224312068800512, '拔毛-5*5', 45, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 中级造型师
(1700078388884148236, 1698304759049409026, 1623214274942091265, '拔毛+娃娃发-4*4', 30, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 中级造型师
(1700078388888342528, 1698304759049409026, 1700043940671094784, '拔毛+娃娃发-5*5', 30, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 中级造型师
(1700078388888342529, 1698304759049409026, 1623214517632909313, '娃娃发-13*4', 30, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 中级造型师
(1700078388888342530, 1698304759049409026, 1623214452860272641, '拔毛-13*4', 30, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 中级造型师
(1700078388888342531, 1698304759049409026, 1623214341522472962, '拔毛+娃娃发-13*4', 18, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 中级造型师
(1700078388888342532, 1698304759049409026, 1623213919458050050, '返修加工-简单造型', 40, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 中级造型师
(1700078388888342533, 1698304759049409026, 1623213850897956866, '返修加工-中等造型', 30, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 中级造型师
(1700078388888342534, 1698304759049409026, 1623213766680526850, '返修加工-复杂造型', 15, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 低级缝卡子
(1700078388888342535, 1698304759049409032, 1623218483590610945, '只缝松紧带', 150, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 低级缝卡子
(1700078388888342536, 1698304759049409032, 1623218415856795650, '缝马尾卡子', 150, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 低级缝卡子
(1700078388888342537, 1698304759049409032, 1623216294562381826, '拆/缝松紧带', 150, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 低级缝卡子
(1700078388888342538, 1698304759049409032, 1623218713312641025, '4个梳卡+松紧带', 90, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 低级缝卡子
(1700078388888342539, 1698304759049409032, 1623217775017476097, '缝U-part卡子（4个九齿卡）', 90, current_timestamp,
 'SYSTEM', current_timestamp, 'SYSTEM', 0, 0),
-- 低级缝卡子
(1700078388888342540, 1698304759049409032, 1623217883410874369, '缝半头套卡子', 60, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 低级缝卡子
(1700078388888342541, 1698304759049409032, 1623217928487059457, '缝Vpart卡子+松紧带', 60, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 低级缝卡子
(1700078388888342542, 1698304759049409032, 1623216547617325058, '卡子发', 9, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 低级缝头套
(1700078388888342543, 1698304759049409034, 1623216714592567297, '刘海片', 100, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 低级缝头套
(1700078388888342544, 1698304759049409034, 1623216603472871426, '抽绳马尾', 75, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 低级缝头套
(1700078388888342545, 1698304759049409034, 1623216661186494466, '半圆马尾', 75, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 低级缝头套
(1700078388888342546, 1698304759049409034, 1623218927167619073, '2*6头套', 35, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 低级缝头套
(1700078388888342547, 1698304759049409034, 1638236227889332224, '4*4头套', 30, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 低级缝头套
(1700078388888342548, 1698304759049409034, 1623219466101518338, '5*5头套', 30, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 低级缝头套
(1700078388892536832, 1698304759049409034, 1623219415312691201, '6*6头套', 30, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 低级缝头套
(1700078388892536833, 1698304759049409034, 1623219361197780994, '13*4头套', 30, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 低级缝头套
(1700078388892536834, 1698304759049409034, 1623219041696673793, '13*6头套', 30, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 低级缝头套
(1700078388892536835, 1698304759049409034, 1623219309985329153, 'U型头套', 35, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 低级缝头套
(1700078388892536836, 1698304759049409034, 1623219146365530113, '纯机制头套', 26, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 低级缝头套
(1700078388892536837, 1698304759049409034, 1623219095195021313, '冰丝发带头套', 26, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 低级缝头套
(1700078388892536838, 1698304759049409034, 1623218876680781825, 'V-part头套', 26, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 低级缝头套
(1700078388892536839, 1698304759049409034, 1623218823031439361, '半头套', 30, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 低级缝头套
(1700078388892536840, 1698304759049409034, 1623218771806404609, '发缝头套', 30, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 低级缝头套
(1700078388892536841, 1698304759049409034, 1623218981508411393, '镂空360头套', 22, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 初级染发师
(1700078388892536842, 1698304759049409030, 1623217412793188354, '染色-简单', 30, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 初级染发师
(1700078388892536843, 1698304759049409030, 1623217353850634242, '染色-中等', 18, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 初级染发师
(1700078388892536844, 1698304759049409030, 1623217172354711554, '染色-发帘发块（简单）', 25, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 初级染发师
(1700078388892536845, 1698304759049409030, 1623217123927277569, '染色-发帘发块（复杂）', 18, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 初级染发师
(1700078388892536846, 1698304759049409030, 1623217516585435137, '离子烫-发帘发块', 50, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 初级染发师
(1700078388892536847, 1698304759049409030, 1623217462604742657, '离子烫-头套', 25, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 初级染发师
(1700078388892536848, 1698304759049409030, 1623217066020716545, '清洗-简单', 80, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 初级染发师
(1700078388892536849, 1698304759049409030, 1623216932469882882, '清洗-复杂', 25, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 初级染发师
(1700078388892536850, 1698304759049409030, 1623221647034740738, '漂扣-4*4', 50, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 初级染发师
(1700078388892536851, 1698304759049409030, 1623220770664280066, '漂扣-5*5', 50, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 初级染发师
(1700078388892536852, 1698304759049409030, 1623221541606715394, '漂扣-13*4', 35, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 初级造型师
(1700078388896731136, 1698304759049409027, 1623215155074842625, '造型-简单', 40, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 初级造型师
(1700078388896731137, 1698304759049409027, 1623215101173841922, '造型-中等', 30, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 初级造型师
(1700078388896731138, 1698304759049409027, 1623214914481176578, '娃娃发-4*4', 60, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 初级造型师
(1700078388896731139, 1698304759049409027, 1638224533016346624, '娃娃发-5*5', 60, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 初级造型师
(1700078388896731140, 1698304759049409027, 1623214848148258817, '拔毛-4*4', 45, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 初级造型师
(1700078388896731141, 1698304759049409027, 1638224312068800512, '拔毛-5*5', 45, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 初级造型师
(1700078388896731142, 1698304759049409027, 1623214274942091265, '拔毛+娃娃发-4*4', 30, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 初级造型师
(1700078388896731143, 1698304759049409027, 1700043940671094784, '拔毛+娃娃发-5*5', 30, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 初级造型师
(1700078388896731144, 1698304759049409027, 1623214517632909313, '娃娃发-13*4', 30, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 初级造型师
(1700078388896731145, 1698304759049409027, 1623214452860272641, '拔毛-13*4', 30, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 初级造型师
(1700078388896731146, 1698304759049409027, 1623214341522472962, '拔毛+娃娃发-13*4', 18, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 初级造型师
(1700078388896731147, 1698304759049409027, 1623213919458050050, '返修加工-简单造型', 40, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 初级造型师
(1700078388896731148, 1698304759049409027, 1623213850897956866, '返修加工-中等造型', 30, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 初级造型师
(1700078388896731149, 1698304759049409027, 1623213766680526850, '返修加工-复杂造型', 15, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 高级染发师
(1700078388896731150, 1698304759049409028, 1623217412793188354, '染色-简单', 30, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 高级染发师
(1700078388896731151, 1698304759049409028, 1623217353850634242, '染色-中等', 18, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 高级染发师
(1700078388896731152, 1698304759049409028, 1691036171779493888, '染色-简单/复杂', 15, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 高级染发师
(1700078388896731153, 1698304759049409028, 1623217286259425281, '染色-复杂', 12, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 高级染发师
(1700078388896731154, 1698304759049409028, 1623217233692213249, '染色-超级复杂', 0, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 高级染发师
(1700078388900925440, 1698304759049409028, 1623217172354711554, '染色-发帘发块（简单）', 25, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 高级染发师
(1700078388900925441, 1698304759049409028, 1623217123927277569, '染色-发帘发块（复杂）', 18, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 高级染发师
(1700078388900925442, 1698304759049409028, 1623217516585435137, '离子烫-发帘发块', 50, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 高级染发师
(1700078388900925443, 1698304759049409028, 1623217462604742657, '离子烫-头套', 25, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 高级染发师
(1700078388900925444, 1698304759049409028, 1623217066020716545, '清洗-简单', 80, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 高级染发师
(1700078388900925445, 1698304759049409028, 1623216932469882882, '清洗-复杂', 25, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 高级染发师
(1700078388900925446, 1698304759049409028, 1623221647034740738, '漂扣-4*4', 50, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 高级染发师
(1700078388900925447, 1698304759049409028, 1623220770664280066, '漂扣-5*5', 50, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 高级染发师
(1700078388900925448, 1698304759049409028, 1623221541606715394, '漂扣-13*4', 35, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 高级造型师
(1700078388900925449, 1698304759049409025, 1623215155074842625, '造型-简单', 40, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 高级造型师
(1700078388900925450, 1698304759049409025, 1623215101173841922, '造型-中等', 30, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 高级造型师
(1700078388900925451, 1698304759049409025, 1623215042914959362, '造型-复杂', 15, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 高级造型师
(1700078388900925452, 1698304759049409025, 1623214120398766081, '造型-超级复杂', 12, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 高级造型师
(1700078388900925453, 1698304759049409025, 1623214914481176578, '娃娃发-4*4', 60, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 高级造型师
(1700078388900925454, 1698304759049409025, 1638224533016346624, '娃娃发-5*5', 60, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 高级造型师
(1700078388900925455, 1698304759049409025, 1623214848148258817, '拔毛-4*4', 45, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 高级造型师
(1700078388900925456, 1698304759049409025, 1638224312068800512, '拔毛-5*5', 45, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 高级造型师
(1700078388900925457, 1698304759049409025, 1623214274942091265, '拔毛+娃娃发-4*4', 30, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 高级造型师
(1700078388900925458, 1698304759049409025, 1700043940671094784, '拔毛+娃娃发-5*5', 30, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 高级造型师
(1700078388900925459, 1698304759049409025, 1623214517632909313, '娃娃发-13*4', 30, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 高级造型师
(1700078388900925460, 1698304759049409025, 1623214452860272641, '拔毛-13*4', 30, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 高级造型师
(1700078388905119744, 1698304759049409025, 1623214341522472962, '拔毛+娃娃发-13*4', 18, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 高级造型师
(1700078388905119745, 1698304759049409025, 1623213919458050050, '返修加工-简单造型', 40, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 高级造型师
(1700078388905119746, 1698304759049409025, 1623213850897956866, '返修加工-中等造型', 30, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0),
-- 高级造型师
(1700078388905119747, 1698304759049409025, 1623213766680526850, '返修加工-复杂造型', 15, current_timestamp, 'SYSTEM',
 current_timestamp, 'SYSTEM', 0, 0);



