INSERT INTO Menu (MENUID,DATECREATED,DATEMODIFIED,MENUACTION,MENUNAME,MENUORDER,PARENTMENUID) VALUES (1,{ts '2015-06-09 00:00:00.0'},{ts '2015-06-09 00:00:00.0'},'Home.ftl','Home',1,0);
INSERT INTO Menu (MENUID,DATECREATED,DATEMODIFIED,MENUACTION,MENUNAME,MENUORDER,PARENTMENUID) VALUES (3,{ts '2015-06-09 00:00:00.0'},{ts '2015-06-09 00:00:00.0'},null,'Configure Test',2,0);
INSERT INTO Menu (MENUID,DATECREATED,DATEMODIFIED,MENUACTION,MENUNAME,MENUORDER,PARENTMENUID) VALUES (13,{ts '2015-06-09 00:00:00.0'},{ts '2015-06-09 00:00:00.0'},'MiscellaneousToolsHome.ftl','Miscellaneous Tools',3,0);
INSERT INTO Menu (MENUID,DATECREATED,DATEMODIFIED,MENUACTION,MENUNAME,MENUORDER,PARENTMENUID) VALUES (2,{ts '2015-06-09 00:00:00.0'},{ts '2015-06-09 00:00:00.0'},null,'Administration',4,0);
INSERT INTO Menu (MENUID,DATECREATED,DATEMODIFIED,MENUACTION,MENUNAME,MENUORDER,PARENTMENUID) VALUES (4,{ts '2015-06-09 00:00:00.0'},{ts '2015-06-09 00:00:00.0'},null,'Help',5,0);

INSERT INTO Menu (MENUID,DATECREATED,DATEMODIFIED,MENUACTION,MENUNAME,MENUORDER,PARENTMENUID) VALUES (5,{ts '2015-02-10 00:00:00.0'},{ts '2015-02-10 00:00:00.0'},'UiTestingSetup.ftl','Web Application',1,2);
INSERT INTO Menu (MENUID,DATECREATED,DATEMODIFIED,MENUACTION,MENUNAME,MENUORDER,PARENTMENUID) VALUES (6,{ts '2015-02-10 00:00:00.0'},{ts '2015-02-10 00:00:00.0'},'UserAccessSetup.ftl','User Access',2,2);
INSERT INTO Menu (MENUID,DATECREATED,DATEMODIFIED,MENUACTION,MENUNAME,MENUORDER,PARENTMENUID) VALUES (8,{ts '2015-06-09 00:00:00.0'},{ts '2015-06-09 00:00:00.0'},'FunctionalityTesting.ftl','Functional',1,3);
INSERT INTO Menu (MENUID,DATECREATED,DATEMODIFIED,MENUACTION,MENUNAME,MENUORDER,PARENTMENUID) VALUES (9,{ts '2015-06-09 00:00:00.0'},{ts '2015-06-09 00:00:00.0'},'AnalyticsTesting.ftl','Analytics',2,3);
INSERT INTO Menu (MENUID,DATECREATED,DATEMODIFIED,MENUACTION,MENUNAME,MENUORDER,PARENTMENUID) VALUES (10,{ts '2015-06-09 00:00:00.0'},{ts '2015-06-09 00:00:00.0'},'BrokenLinkCheck.ftl','Broken Link Check',3,3);
INSERT INTO Menu (MENUID,DATECREATED,DATEMODIFIED,MENUACTION,MENUNAME,MENUORDER,PARENTMENUID) VALUES (11,{ts '2015-06-09 00:00:00.0'},{ts '2015-06-09 00:00:00.0'},'SetupApplication.ftl','Akamai',4,3);
INSERT INTO Menu (MENUID,DATECREATED,DATEMODIFIED,MENUACTION,MENUNAME,MENUORDER,PARENTMENUID) VALUES (12,{ts '2015-06-09 00:00:00.0'},{ts '2015-06-09 00:00:00.0'},'SetupApplication.ftl','WebService',5,3);


INSERT INTO Groups (groupid,groupname,groupdescription,groupAdminRights) VALUES (1,'Admin','Admin',1);
INSERT INTO Groups (groupid,groupname,groupdescription,groupAdminRights) VALUES (2,'ProdSupport','Production Support',0);
INSERT INTO Users (userId,groupId,userName,emailId) VALUES (1,1,'admin','dmanni@tiaa-cref.org');
INSERT INTO Users (userId,groupId,userName,emailId) VALUES (2,1,'manni','dmanni@tiaa-cref.org');
INSERT INTO UserGroupMenuReference (userMenuGroupRefId,menuId,groupId) VALUES (1,1,1);
INSERT INTO UserGroupMenuReference (userMenuGroupRefId,menuId,groupId) VALUES (2,2,1);
INSERT INTO UserGroupMenuReference (userMenuGroupRefId,menuId,groupId) VALUES (3,3,1);
INSERT INTO UserGroupMenuReference (userMenuGroupRefId,menuId,groupId) VALUES (4,4,1);
INSERT INTO UserGroupMenuReference (userMenuGroupRefId,menuId,groupId) VALUES (5,5,1);
INSERT INTO UserGroupMenuReference (userMenuGroupRefId,menuId,groupId) VALUES (6,6,1);
INSERT INTO UserGroupMenuReference (userMenuGroupRefId,menuId,groupId) VALUES (7,7,1);
INSERT INTO UserGroupMenuReference (userMenuGroupRefId,menuId,groupId) VALUES (8,8,1);
INSERT INTO UserGroupMenuReference (userMenuGroupRefId,menuId,groupId) VALUES (9,9,1);
INSERT INTO UserGroupMenuReference (userMenuGroupRefId,menuId,groupId) VALUES (10,10,1);
INSERT INTO UserGroupMenuReference (userMenuGroupRefId,menuId,groupId) VALUES (11,11,1);
INSERT INTO UserGroupMenuReference (userMenuGroupRefId,menuId,groupId) VALUES (12,12,1);
INSERT INTO UserGroupMenuReference (userMenuGroupRefId,menuId,groupId) VALUES (18,13,1);

INSERT INTO UserGroupMenuReference (userMenuGroupRefId,groupId,menuId) VALUES (13,2,1);
INSERT INTO UserGroupMenuReference (userMenuGroupRefId,groupId,menuId) VALUES (14,2,2);
INSERT INTO UserGroupMenuReference (userMenuGroupRefId,groupId,menuId) VALUES (15,2,3);
INSERT INTO UserGroupMenuReference (userMenuGroupRefId,groupId,menuId) VALUES (16,2,5);
INSERT INTO UserGroupMenuReference (userMenuGroupRefId,groupId,menuId) VALUES (17,2,8);
INSERT INTO UserGroupMenuReference (userMenuGroupRefId,groupId,menuId) VALUES (19,2,13);

INSERT INTO SolutionType (SolutionTypeId,SolutionTypeName) VALUES (1,'Web Application');
INSERT INTO SolutionType (SolutionTypeId,SolutionTypeName) VALUES (2,'Web Service');
INSERT INTO SolutionType (SolutionTypeId,SolutionTypeName) VALUES (3,'Miscelaneous Tools');

INSERT INTO EnvironmentCategory (EnvironmentCategoryId,EnvironmentCategoryName) VALUES (1, 'ALL');
INSERT INTO EnvironmentCategory (EnvironmentCategoryId,EnvironmentCategoryName) VALUES (2, 'ST2');
INSERT INTO EnvironmentCategory (EnvironmentCategoryId,EnvironmentCategoryName) VALUES (3, 'ST4');
INSERT INTO EnvironmentCategory (EnvironmentCategoryId,EnvironmentCategoryName) VALUES (4, 'AT');
INSERT INTO EnvironmentCategory (EnvironmentCategoryId,EnvironmentCategoryName) VALUES (5, 'PROD_FIX');
INSERT INTO EnvironmentCategory (EnvironmentCategoryId,EnvironmentCategoryName) VALUES (6, 'PROD');
INSERT INTO EnvironmentCategory (EnvironmentCategoryId,EnvironmentCategoryName) VALUES (7, 'DEV_INT');
INSERT INTO EnvironmentCategory (EnvironmentCategoryId,EnvironmentCategoryName) VALUES (8, 'PROD_PREV');

INSERT INTO MODULETYPE (MODULETypeID,MODULETYPE) VALUES (1,'URL Pattern');
INSERT INTO MODULETYPE (MODULETypeID,MODULETYPE) VALUES (2,'Static URL');
INSERT INTO MODULETYPE (MODULETypeID,MODULETYPE) VALUES (3,'Transactional Test');

INSERT INTO functionalitypermission (functionalityId,functionalityName,menuId) VALUES (1,'Edit Application',5);
INSERT INTO functionalitypermission (functionalityId,functionalityName,menuId) VALUES (2,'Edit Environment',5);
INSERT INTO functionalitypermission (functionalityId,functionalityName,menuId) VALUES (3,'Update Access',6);
INSERT INTO functionalitypermission (functionalityId,functionalityName,menuId) VALUES (4,'Show All UI Suits',1);
INSERT INTO functionalitypermission (functionalityId,functionalityName,menuId) VALUES (5,'Show Set Alias',1);
INSERT INTO functionalitypermission (functionalityId,functionalityName,menuId,toolId) VALUES (6,'Clear Cache',13,1);
INSERT INTO functionalitypermission (functionalityId,functionalityName,menuId,toolId) VALUES (7,'OPRA Reset',13,2);
INSERT INTO functionalitypermission (functionalityId,functionalityName,menuId,toolId) VALUES (8,'Manage Suits',1,1);
INSERT INTO functionalitypermission (functionalityId,functionalityName,menuId) VALUES (10,'Monitor Pending Jobs',1);
INSERT INTO functionalitypermission (functionalityId,functionalityName,menuId,toolId) VALUES (11,'OPRA MVC Reset',13,3);
INSERT INTO functionalitypermission (functionalityId,functionalityName,menuId,toolId) VALUES (12,'ESB Service Tool',13,4);
INSERT INTO functionalitypermission (functionalityId,functionalityName,menuId,toolId) VALUES (13,'Usage Reports',13,5);
INSERT INTO functionalitypermission (functionalityId,functionalityName,menuId,toolId) VALUES (14,'ESB Ping Add Service',13,null);
INSERT INTO functionalitypermission (functionalityId,functionalityName,menuId) VALUES (15,'MintV4 Suit',1);


INSERT INTO BROWSERTYPE (BROWSERTYPEID,BROWSERNAME) VALUES (1,'Firefox');
INSERT INTO BROWSERTYPE (BROWSERTYPEID,BROWSERNAME) VALUES (2,'Chrome');
INSERT INTO BROWSERTYPE (BROWSERTYPEID,BROWSERNAME) VALUES (3,'IE');
INSERT INTO BROWSERTYPE (BROWSERTYPEID,BROWSERNAME) VALUES (4,'Safari');

INSERT INTO MiscellaneousTool (toolId,toolName,toolDescription,toolActionUrl) VALUES (1,'Clear Cache','Tool For Clearing Cache Execution','ClearCacheHome.ftl');
INSERT INTO MiscellaneousTool (toolId,toolName,toolDescription,toolActionUrl) VALUES (2,'OPRA Reset','Tool For Resetting Pins','OpraReset.ftl');
INSERT INTO MiscellaneousTool (toolId,toolName,toolDescription,toolActionUrl) VALUES (3,'OPRA MVC Reset','Tool For Resetting Pins','OpraMVCReset.ftl');
INSERT INTO MiscellaneousTool (toolId,toolName,toolDescription,toolActionUrl) VALUES (4,'ESB Service Tool','Tool For testing ESB','ESBServiceSetupHome.ftl');
INSERT INTO MiscellaneousTool (toolId,toolName,toolDescription,toolActionUrl) VALUES (5,'Usage Reports','Tool For Usage Reports','UsageReports.ftl');

INSERT INTO FUNCTIONALITYGROUPREF (FUNCTIONALITYGROUPREFID,FUNCTIONALITY_ID,GROUP_ID) VALUES (1,4,1);
INSERT INTO FUNCTIONALITYGROUPREF (FUNCTIONALITYGROUPREFID,FUNCTIONALITY_ID,GROUP_ID) VALUES (2,5,1);
INSERT INTO FUNCTIONALITYGROUPREF (FUNCTIONALITYGROUPREFID,FUNCTIONALITY_ID,GROUP_ID) VALUES (3,6,1);
INSERT INTO FUNCTIONALITYGROUPREF (FUNCTIONALITYGROUPREFID,FUNCTIONALITY_ID,GROUP_ID) VALUES (4,8,1);
INSERT INTO FUNCTIONALITYGROUPREF (FUNCTIONALITYGROUPREFID,FUNCTIONALITY_ID,GROUP_ID) VALUES (5,10,1);
INSERT INTO FUNCTIONALITYGROUPREF (FUNCTIONALITYGROUPREFID,FUNCTIONALITY_ID,GROUP_ID) VALUES (6,14,1);

INSERT INTO menu (menuId,dateCreated,dateModified,menuAction,menuName,menuOrder,parentMenuId) VALUES (14,{d '2015-07-31'},{d '2015-07-31'},'WebserviceSetup.ftl','Webservice',3,2);
INSERT INTO usergroupmenureference (userMenuGroupRefId,groupId,menuId) VALUES (30,1,14);

INSERT INTO excludelinktype (excludeLinktypeId,excludeLinktype) VALUES (1,'Testing001');
INSERT INTO excludelinktype (excludeLinktypeId,excludeLinktype) VALUES (2,'Testing002');
INSERT INTO menu (menuId,dateCreated,dateModified,menuAction,menuName,menuOrder,parentMenuId) VALUES (15,{d '2015-12-15'},{d '2015-12-15'},'UiFindImageOrText.ftl','Find Image/Text',6,3);
INSERT INTO miscellaneoustool (toolId,toolActionUrl,toolDescription,toolName) VALUES (6,'ArchiveData.ftl','Tool for Archive Data','Archive Data');
INSERT INTO functionalitypermission (functionalityId,functionalityName,menuId,toolId) VALUES (15,'Archive Data',13,6);
INSERT INTO functionalitygroupref (functionalityGroupRefId,functionality_id,group_id) VALUES (62,15,1);

update suit set functionalityTypeId = 3 where type = "Analytics";
update suit set functionalityTypeId = 4 where type = "Broken";
update suit set functionalityTypeId = 8 where type = "TextOrImage";
update suit set functionalityTypeId = 1 where type = "Functional";
