(function () {
    'use strict';
    angular.module('BinaryOptions')
        .constant('CONSTANT', CONSTANT());

    function CONSTANT() {
        // noinspection SpellCheckingInspection
        var company = {
            caifuhui: {
                pre: "走势宝"
                // last: 'Option'
            }
        };

        var _distName = company.caifuhui;

        var ENDPOINT = {
                host: 'https://i.api789.top',
                port: 8080,
                // host: 'http://192.168.1.163',
                // port: 1008,
                exportMap: 9101,//导图
                path: '/indexMark',
                T: 200, //动作id
                needsAuth: false
                //});
            },
            SIGNALR = {
                host: 'https://i.api789.top',
                port: 8080,
                // host: 'http://192.168.1.163',
                // port: 1008,
                exportMap: 7000,//导图
                path: '/indexMark',
                T: 200, //动作id
                needsAuth: false
            },
            System = {ver: 0, name: _distName.pre, os: 'ios', platform: '9', currencies: 7.0},
            Company = {preName: _distName.pre, lastName: _distName.last, website: 'www.kmoption.com'},
            SING = {
                host: 'https://i.api789.top',
                //host: 'http://192.168.1.42',
                port: 1009,
                path: '/indexMark',
                T: 200, //动作id
                needsAuth: false
            },
            STARTUPURL = 'https://j.api789.top',
            //STARTUPURL = 'http://192.168.1.127',
            POPUPSTATUS = {
                Open: 1,//开启状态
                Close: 2//关闭状态
            },
            LOCALSTORAGE = {
                User: 'user',
                Products: 'Products',//产品信息列表
                BuyWay: 'buyway',//购买方式
                IsHasRegister: 'isHasRegister',//是否注册
                TryPlayTimes: 'TryPlayTimes',//试玩次数
                IsHasGuid: 'IsHasGuid',
                NewsStore: 'NewsStore'
            },
            RESPONSE = {
                Success: 101,
                StatusSuccess: 0,
                Error: 201,
                LoginMiss: 104
            },
            TACTION = {
                NormalData: 200, //普通数据请求
                RateData: 0, //指数动作代码
                TimeRateData: 20, //历史段指数请求
                CancelData: 10, //取消指数请求动作
                PrizePoint: 12,
                IssueData: 300,//期号
                OpenOrder: 500,//开奖
                IssueBounusNotify: 550,//每一期开奖
                IssuePrized: 900,
                AutoRegister: 1101//自动注册
            },
            BROADCAST = {
                UpdateUserInfo: 'UpdateUserInfo',
                UpdateUserTypeInfo: 'UpdateUserTypeInfo',
                UpdateTypeChosen: 'UpdateTypeChosen',
                UpdateIssueTrend: 'UpdateIssueTrend',//更新走势图上的期号线
                BuySuccess: 'buyInexMarks', //购买成功
                UpdateCountDownTime: 'updateCountDownTime', //更新倒计时
                UpdateIssue: 'updateIssue',//更新期号
                NetWorkError: 'NetWorkError',//网路问题
                NetWorkConnect: 'NetWorkConnect',//网络,
                ReceiveMoney: 'ReceiveMoney',
                Reset: 'Reset',//清除购买记录
                ChartStopScaleBig: 'stopScaleBig',
                ChartStopScaleSmall: 'stopScaleSmall',
                ChartScaleNormal: 'chartScaleNormal',
                ScrollEvent: 'scrollEvent',// 发送scroll event，用于auto-top 指令
                ClearIssue: 'ClearIssue',
                ApiConnectFail: 'ApiConnectFail',//网络连接失败
                ApiConnectSuccess: 'ApiConnectSuccess',
                CloseTradePopup: 'closeTradeIssueDialog'
            },
            AccountType = {
                Bank: 100,//网银
                Alipay: 101,//支付宝
                WeChat: 103//微信
            },
            Broadcast = {
                Send: 100
            },
            Dialog = {
                OpenLogin: 'openLogin',
                CloseLogin: 'closeLogin',
                OpenReg: 'openReg',
                CloseReg: 'closeReg',
                OpenGiver: 'openGiver',
                CloseGiver: 'closeGiver',
                OpenComfirm: 'openComfirm',
                CloseComfirm: 'closeComfirm',
                OpenPrompt: 'openPrompt',
                ClosePrompt: 'closePrompt',
                OpenGiverOver: 'openGiverOver',
                CloseGiverOver: 'closeGiverOver',
                Close: 'closeDialog'
            },
            Flag = {
                GiverCount: 'givers'
            },
            BonusLevel = {
                One: 200,
                Two: 500,
                Three: 1000
            },
            URL = {
                clock: './images/clock.png',//'http://192.168.1.222:9010/images/clock.png',
                flag: './images/flag.png',//'http://192.168.1.222:9010/images/flag.png',//
                raseUp: 'url(./images/up.png)',//买涨//'url(http://192.168.1.222:9010/images/up.png)',//
                raseDown: 'url(./images/down.png)',//买跌//'url(http://192.168.1.222:9010/images/down.png)'//
                chartBackgroundImage: './images/map.png'//走势图背景
            },
            DELAYTIME = {
                ProductDelay: 60000,
                ChartBgDelay: 2000
            },
            VALUE = {
                BuyMaxMoney: 1500,//购买金额最大值
                BuyMinMoney: 1,//购买金额最小值
                RateDataTimes: 10000,//投注比例请求频率
                ChargeMaxMoney: 700,
                ChargeMinMoney: 1
            },
            ISSUETYPE = {
                IssueType0: {
                    IssueType: 0,
                    IssueTypeName: '60秒',
                    IssueTypeVal: '60'
                },
                IssueType1: {
                    IssueType: 1,
                    IssueTypeName: '30秒',
                    IssueTypeVal: '30'
                },
                IssueType2: {
                    IssueType: 2,
                    IssueTypeName: '90秒',
                    IssueTypeVal: '90'
                }
            },
            PRODUCTID = {
                Gold: 100, //黄金
                Silver: 1, //白银
                Oil: 2 //石油
            },
            ProductGroup = {
                GP: 'GP',
                WH: 'WH',
                GJS: 'GJS',
                OTC: 'OTC'
            },
            STRING = {
                DialogTitle: '系统提示',
                TradeDialogTitle: '交易确定',
                Ok: '确 定',
                Cancel: '取 消',
                LoginTradeTip: '您尚未登录，请登录后再进行交易!',
                LoginInvalid: '用户登录过期，请重新登录',
                AccountNotEnough: '余额不足，请充值!',
                Timeout: '请求超时!',
                NotHaveNetWork: '请检查网络是否连接!',
                NetWorkError: '暂未获取到数据，请检查是否网络存在',
                dollar: '$',
                RMB: '￥'
            },
            ORDERSTATUS = {
                Receive: 0, //收单中
                Received: 1, //收单完成
                Payment: 2, //付款中
                Ticketed: 20, //付款完成，订单确认
                Bounusing: 30, //收益派发中
                Bounused: 40, //收益派发完成
                IssueNotExist: 94, //期号不存在
                IssueError: 95, //期号错误
                AccountNotEnough: 96, //余额不足，扣费失败
                Error: 99 //有错误发生
            },
            UIEVENT = {
                OnLoading: 'OnLoading',
                OnResume: 'OnResume',
                UpdateMoney: 'UpdateMoney',
                MainInfo: 'MainInfo',
                UpdateUser: 'UpdateUser',
                OnNotice: 'OnNotice',

            },
            PAGE = {
                home: '首页',
                bankCards: '我的银行卡',
                userCenter: '我的账户',
                tradeLog: '交易记录',
                fundLog: '收支明细',
                withdrawLog: '提款明细',
                rechargeLog: '充值明细',
                instructions: '使用说明',
                demo: '新手指导',
                professional: '专业术语',
                professionDetail: '术语详解',
                customerService: '客服',
                changePassword: '修改密码',
                tradeCenter: '交易中心',
                accountDetail: '账户明细',
                useManual: '使用说明',
                professionalTerm: '专业术语',
                userHelp: '客服',
                wechatShare: '微信分享',
                modifyInfo: '个人资料',
                charge: '充值',
                withdraw: '提款页面',
                bindRealName: '实名认证',
                bindBankCards: '绑定银行卡',
                withdrawInfoDetail: '提款明细详情',
                unBindBankCard: '解除绑定',
                welcome: 'welcome',
                startLin: 'startLin',
                startRegister: '注册',
                startLogin: '登录',
                setting: '设置',
                sidemenu: '侧边栏',
                newsModel: '期货行情',
                tutorial: '攻略',
                news: '资讯',
                account: '我的'

            },
            STYLE = {
                init: '',
                Chosen: {
                    'border-width': '1px',
                    'border-style': 'solid',
                    'border-color': '#349644'
                },
                normalButton: {
                    'box-shadow': '0 0 0.4em #42A5f5'
                },
                errorButton: {
                    'box-shadow': '0 0 0.4em #FF3333'
                },
                WarNumber: {
                    'border-width': '1px',
                    'border-style': 'solid',
                    'border-color': '#FF8533'
                },
                MaxNumber: {
                    'border-width': '1px',
                    'border-style': 'solid',
                    'border-color': '#FF3333'
                }
            },
            BettingMoney = {
                MaxMoney: 400,
                MinMoney: 10,
                reduceOrAddMoney: 10
            },
            API_STATUS = {
                list: [
                    {Status: 0, Msg: '成功'},
                    {Status: 1, Msg: '未知错误'},
                    {Status: 9, Msg: 'SIGN验证失败'},
                    {Status: 10, Msg: '参数验证失败'},
                    {Status: 11, Msg: '平台验证失败'},
                    {Status: 100, Msg: '参数必填'},
                    {Status: 11, Msg: '平台验证失败'},
                    {Status: 101, Msg: '用户名或密码错误'},
                    {Status: 102, Msg: '用户名已存在'},
                    {Status: 103, Msg: 'IP限制'},
                    {Status: 104, Msg: '用户TOKEN验证失败'},
                    {Status: 105, Msg: '用户不存在'},
                    {Status: 106, Msg: '密码错误'},
                    {Status: 107, Msg: '支付密码与登录密码不能相同'},
                    {Status: 151, Msg: '用户已经绑定过真实姓名'},
                    {Status: 152, Msg: '用户已经绑定过银行卡'},
                    {Status: 153, Msg: '用户已经绑定过手机号码'},
                    {Status: 154, Msg: '用户手机绑定失败'},
                    {Status: 155, Msg: '银行卡开户人,真实姓名不匹配'},
                    {Status: 156, Msg: '验证码错误'},
                    {Status: 157, Msg: '请求不存在'},
                    {Status: 158, Msg: '请求已超时'},
                    {Status: 159, Msg: '请求已存在'},
                    {Status: 201, Msg: '账户被冻结'},
                    {Status: 202, Msg: '账户余额不足'},
                    {Status: 203, Msg: '账户操作失败'},
                    {Status: 204, Msg: '账户类型不存在'},
                    {Status: 404, Msg: '方案不存在'},
                    {Status: 405, Msg: '合买方案剩余可购买份数不足'},
                    {Status: 406, Msg: '方案金额错误'},
                    {Status: 407, Msg: '方案状态不允许参与合买 '}
                ]
            },
            LOCALSTORAGE_KEY = {
                UserToken: 'UserToken',
                UserInfo: 'user',
                UserType: 'userType',
                TypeChosen: 'typeChosen',
                UpdateHome: 'updateHome',
                IsLoginOut: 'IsLoginOut',
                CloseTime: 'CloseTime',
                ReLogin: 'ReLogin',
                Register: {
                    Name: 'name',
                    Password: 'password',
                    InvitedCode: 'invitedCode'
                }
            },
            BANK_INFO = {
                list: [
                    {value: 5, text: '中国银行'},
                    {value: 1, text: '中国建设银行'},
                    {value: 2, text: '中国农业银行'},
                    {value: 22, text: '中国工商银行'},
                    {value: 21, text: '中国招商银行'},
                    {value: 3, text: '交通银行'},
                    {value: 4, text: '中信银行'},
                    {value: 6, text: '中国民生银行'},
                    {value: 7, text: '广东发展银行'},
                    {value: 8, text: '宁波银行'},
                    {value: 9, text: '深圳发展银行'},
                    {value: 10, text: '兴业银行'},
                    {value: 11, text: '中国广大银行'},
                    {value: 12, text: '上海浦发银行'},
                    {value: 13, text: '平安银行'},
                    {value: 14, text: '北京银行'},
                    {value: 15, text: '上海农商银行'},
                    {value: 16, text: '中国邮政储蓄银行'},
                    {value: 17, text: '杭州银行'},
                    {value: 18, text: '上海银行'},
                    {value: 19, text: '北京农村商业银行'},
                    {value: 20, text: '富滇银行'}]
            },
            PAYMENT_TYPE = {
                Betting: 0,//投注扣款
                ActivityPayment: 50,//活动扣款
                Withdrawal: 60,//提现
                OtherPayment: 99,//其他订单扣款
                NetRecharge: 100,//网银充值
                AlipayRecharge: 101,//支付宝充值
                TenpayRecharge: 102,//财付通充值
                WeiXinRecharge: 103,//微信支付充值
                AlipaySecurityRecharge: 104,//支付宝快捷充值
                AlipayWebRecharge: 105,//支付宝网页充值
                ActivityRecharge: 150,//活动充值
                ManualPayment: 160,//手工扣款
                ManualRecharge: 199,//手工充值
                SystemPrize: 200,//系统返奖
                PlusPrize: 202,//平台加奖
                ManualPrize: 299,//手工补返奖
                WithdrawalNotThrough: 301,//提现不成功，资金返回当前账户
                Refund: 302//退款
            },
            RQUESTCODE = {
                UserRegister: 101,//用户注册
                UserLogin: 102, //用户登录
                UserUpdatePwd: 103, //用户密码修改
                UserUpdate: 104, //用户资料修改
                UserInfo: 105, //获取用户信息
                UserAccountRequest: 106, //用户账户信息查询
                UserEditInfo: 107, //用户账户更改昵称 国籍  头像等
                ResetPwdRequest: 109, //重置用户密码
                UserNameValidate: 110, //用户名验证
                UserPayPwdModify: 111, //支付密码修改
                UserPayPwdRset: 112, //支付密码重置
                SetReceiveMessage: 113, //设置是否接收系统通知
                GetNotReceiveMessageUserId: 114, //获取不接收系统通知用户ID
                RequestResetPassword: 118, //申请重置密码
                ResetPassword: 119,//重置密码
                UserRealNameBind: 120, //用户实名绑定
                UserMobileBind: 122, //手机号绑定
                UserMobileBindValidate: 123, //手机号绑定验证
                UserBankCardBind: 124, //银行卡绑定
                UserUnBindCard: 129,//解除银行卡绑定
                UnionLoginBind: 130, //联合登录绑定
                ResetLoginPwd: 131, //重设登录密码
                FindPwdSmsSendRequest: 132, //找回密码验证码发送接口
                FindPwdSmsVerifyRequest: 133, //找回密码验证码验证接口
                FeedBackRequest: 134, //用户反馈
                ChangeAccountMode: 160,//切换账号模式
                BuyRequest: 201, //购买请求
                BuyRecordsRequest: 202, //购买记录
                CancelOrderRequest: 203,//撤单请求
                UsersTradingRequest: 206,//用户动态和交易动态查询
                HistoryIndexMarkRequest: 210,//历史指数查询
                ratioUpDown: 220, //产品看涨看跌
                IssueRequest: 301, //可用期号查询
                ProductRequest: 310, //产品信息查询
                RechargeRequest: 401, //充值请求
                FundDetailRequest: 405, //资金明细查询请求
                CountInfo: 407, //统计信息
                FundInfoRequest: 406, //资金详情查询WithdrawalRequest
                ManualRechargeRequest: 410, //手工充值请求
                WithdrawalRequest: 420,//提现请求
                WithdrawalInfoRequest: 421, //提现详情
                PromotionShared: 510,//个人推广信息查询
                PromotionMyCustomers: 511,//查询受邀用户列表
                PromotionCommissionCheckout: 513,//推广结算金额转余额
                PromotionMyBounds: 514,//奖金明细
                PromotionReceivePrize: 515,//领取奖金
                PromotionCustomerTrading: 516,//用户交易明细
                PromotionCommissionLog: 517,//个人推广信息查询,
                PromotionSaveShare: 710,//保持分享信息
                PromotionShareLog: 711,//分享日志
                VerificationRequest: 999 //验证码请求

            },
            MoneyType = {
                Cash: 0,//现金账户
                Bonus: 1,//奖金账户
                RedEnvelope: 10,//红包
                Coupon: 20,//优惠券
                Gift: 90//赠送
            },
            UserAccountType = {
                RMB: 0,
                USD: 1,
                GIFTRMB: 10000,
                GIFTUSD: 10001,
                Real: 1,
                Gift: 10001,
                Sim: 999
            },
            ShareAd = [
                'images/share/ads/ad1.png',
                'images/share/ads/ad2.png',
                'images/share/ads/ad3.png',
                'images/share/ads/ad4.png'
            ];

        return {
            ENDPOINT: ENDPOINT,
            System: System,
            Company: Company,
            SIGNALR: SIGNALR,
            SING: SING,
            STARTUPURL: STARTUPURL,
            POPUPSTATUS: POPUPSTATUS,
            AccountType: AccountType,
            Broadcast: Broadcast,
            Dialog: Dialog,
            Flag: Flag,
            BonusLevel: BonusLevel,
            URL: URL,
            DELAYTIME: DELAYTIME,
            VALUE: VALUE,
            PRODUCTID: PRODUCTID,
            ISSUETYPE: ISSUETYPE,
            LOCALSTORAGE: LOCALSTORAGE,
            RESPONSE: RESPONSE,
            TACTION: TACTION,
            BROADCAST: BROADCAST,
            STRING: STRING,
            ORDERSTATUS: ORDERSTATUS,
            UIEVENT: UIEVENT,
            PAGE: PAGE,
            STYLE: STYLE,
            BettingMoney: BettingMoney,
            API_STATUS: API_STATUS,
            LOCALSTORAGE_KEY: LOCALSTORAGE_KEY,
            BANK_INFO: BANK_INFO,
            PAYMENT_TYPE: PAYMENT_TYPE,
            RQUESTCODE: RQUESTCODE,
            MoneyType: MoneyType,
            UserAccountType: UserAccountType,
            ShareAd: ShareAd,
            ProductGroup: ProductGroup
        };

    }

})();
