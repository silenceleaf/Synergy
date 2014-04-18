
<html lang="en">
<head>
    <meta name="description" content="Main Window of App"/>
    <meta name="author" content="zjy"/>
    <meta name="copyright" content="zjy"/>
    <link rel="stylesheet" type="text/css" href="/extjs/resources/css/ext-all-neptune.css"/>
    <script type="text/javascript" src="/extjs/ext-all-dev.js"></script>
    <script type="text/javascript" src="/extjs/locale/ext-lang-en.js"></script>
    <script type="text/javascript" src="/js/util.js"></script>

    <title>Main</title>
</head>

<body>
    <script type="text/javascript">
        (function () {
            Ext.onReady(function () {
                var backgroundPic = Ext.create('Ext.Img', {
                    id: 'backgroundPic',
                    src: 'images/background.jpg',
                    width: '100%',
                    height: '100%',
                    renderTo: Ext.getBody()
                });

                var templateStore = createTemplateStore(200);
                templateStore.load();
                templateStore.on("load", function () {
                    var loginFormPanel = Ext.create('Ext.form.Panel', {
                        title: templateStore.getById(203).get("label"),
                        bodyPadding: 25,
                        width: 400,
                        url: 'login',
                        header: false,
                        buttonAlign: 'center',
                        minButtonWidth: 120,
                        defaults: {
                            padding: '10 0 10 0',
                            height: 30
                        },
                        layout: {
                            type: 'vbox',
                            align: 'center'
                        },
                        items: [
                            {
                                xtype: 'textfield',
                                fieldLabel: templateStore.getById(201).get("text"),
                                name: 'userName'
                            }, {
                                xtype: 'textfield',
                                fieldLabel: templateStore.getById(202).get("text"),
                                name: 'password',
                                inputType: 'password'
                            }, {
                                xtype: 'label',
                                itemId: 'promptLabel'
                            }
                        ],

                        buttons: [
                            {
                                text: templateStore.getById(203).get("text"),
                                handler: function () {
                                    // The getForm() method returns the Ext.form.Basic instance:
                                    var form = loginFormPanel.submit({
                                        clientValidation: true,
                                        url: 'login',
                                        success: function (form, action) {
                                            loginFormPanel.getComponent('promptLabel').setText('<div style="color:red">Success</div>', false);
                                        },
                                        failure: function (form, action) {
                                            switch (action.failureType) {
                                                case Ext.form.action.Action.CLIENT_INVALID:
                                                    loginFormPanel.getComponent('promptLabel').setText('<div style="color:red">Form fields may not be submitted with invalid values</div>', false);
                                                    break;
                                                case Ext.form.action.Action.CONNECT_FAILURE:
                                                    loginFormPanel.getComponent('promptLabel').setText('<div style="color:red">Ajax communication failed</div>', false);
                                                    break;
                                                case Ext.form.action.Action.SERVER_INVALID:
                                                    //Ext.Msg.alert('Failure', promptStore.count());
                                                    loginFormPanel.getComponent('promptLabel').setText(getPromptMsg(action.result.msg), false);
                                                    break;
                                            }
                                        }
                                    });
                                }
                            }
                        ]
                    });

                    Ext.create('Ext.window.Window', {
                        title: templateStore.getById(203).get("text"),
                        titleAlign: 'center',
                        x: window.screen.width * 0.6,
                        y: window.screen.width * 0.1,
                        height: 270,
                        width: 420,
                        resizable: false,
                        closable: false,
                        layout: 'fit',
                        items: [loginFormPanel]
                    }).show();
                });
            })
        })();
    </script>
</body>
</html>