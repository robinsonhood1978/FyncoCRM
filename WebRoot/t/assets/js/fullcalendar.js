!function(n) {
    var t = {};
    function e(r) {
        if (t[r])
            return t[r].exports;
        var o = t[r] = {
            i: r,
            l: !1,
            exports: {}
        };
        return n[r].call(o.exports, o, o.exports, e),
        o.l = !0,
        o.exports
    }
    e.m = n,
    e.c = t,
    e.d = function(n, t, r) {
        e.o(n, t) || Object.defineProperty(n, t, {
            enumerable: !0,
            get: r
        })
    }
    ,
    e.r = function(n) {
        "undefined" != typeof Symbol && Symbol.toStringTag && Object.defineProperty(n, Symbol.toStringTag, {
            value: "Module"
        }),
        Object.defineProperty(n, "__esModule", {
            value: !0
        })
    }
    ,
    e.t = function(n, t) {
        if (1 & t && (n = e(n)),
        8 & t)
            return n;
        if (4 & t && "object" == typeof n && n && n.__esModule)
            return n;
        var r = Object.create(null);
        if (e.r(r),
        Object.defineProperty(r, "default", {
            enumerable: !0,
            value: n
        }),
        2 & t && "string" != typeof n)
            for (var o in n)
                e.d(r, o, function(t) {
                    return n[t]
                }
                .bind(null, o));
        return r
    }
    ,
    e.n = function(n) {
        var t = n && n.__esModule ? function() {
            return n.default
        }
        : function() {
            return n
        }
        ;
        return e.d(t, "a", t),
        t
    }
    ,
    e.o = function(n, t) {
        return Object.prototype.hasOwnProperty.call(n, t)
    }
    ,
    e.p = "/",
    e(e.s = 202)
}({
    1: function(n, t) {
        var e = n.exports = "undefined" != typeof window && window.Math == Math ? window : "undefined" != typeof self && self.Math == Math ? self : Function("return this")();
        "number" == typeof __g && (__g = e)
    },
    10: function(n, t) {
        var e = {}.hasOwnProperty;
        n.exports = function(n, t) {
            return e.call(n, t)
        }
    },
    11: function(n, t, e) {
        var r = e(1)
          , o = e(13)
          , i = e(4)
          , a = e(12)
          , c = e(20)
          , u = function(n, t, e) {
            var l, f, s, d, p = n & u.F, v = n & u.G, m = n & u.S, y = n & u.P, b = n & u.B, g = v ? r : m ? r[t] || (r[t] = {}) : (r[t] || {}).prototype, h = v ? o : o[t] || (o[t] = {}), x = h.prototype || (h.prototype = {});
            for (l in v && (e = t),
            e)
                s = ((f = !p && g && void 0 !== g[l]) ? g : e)[l],
                d = b && f ? c(s, r) : y && "function" == typeof s ? c(Function.call, s) : s,
                g && a(g, l, s, n & u.U),
                h[l] != s && i(h, l, d),
                y && x[l] != s && (x[l] = s)
        };
        r.core = o,
        u.F = 1,
        u.G = 2,
        u.S = 4,
        u.P = 8,
        u.B = 16,
        u.W = 32,
        u.U = 64,
        u.R = 128,
        n.exports = u
    },
    12: function(n, t, e) {
        var r = e(1)
          , o = e(4)
          , i = e(10)
          , a = e(16)("src")
          , c = Function.toString
          , u = ("" + c).split("toString");
        e(13).inspectSource = function(n) {
            return c.call(n)
        }
        ,
        (n.exports = function(n, t, e, c) {
            var l = "function" == typeof e;
            l && (i(e, "name") || o(e, "name", t)),
            n[t] !== e && (l && (i(e, a) || o(e, a, n[t] ? "" + n[t] : u.join(String(t)))),
            n === r ? n[t] = e : c ? n[t] ? n[t] = e : o(n, t, e) : (delete n[t],
            o(n, t, e)))
        }
        )(Function.prototype, "toString", function() {
            return "function" == typeof this && this[a] || c.call(this)
        })
    },
    13: function(n, t) {
        var e = n.exports = {
            version: "2.6.0"
        };
        "number" == typeof __e && (__e = e)
    },
    16: function(n, t) {
        var e = 0
          , r = Math.random();
        n.exports = function(n) {
            return "Symbol(".concat(void 0 === n ? "" : n, ")_", (++e + r).toString(36))
        }
    },
    17: function(n, t) {
        n.exports = function(n) {
            if (null == n)
                throw TypeError("Can't call method on  " + n);
            return n
        }
    },
    19: function(n, t) {
        var e = {}.toString;
        n.exports = function(n) {
            return e.call(n).slice(8, -1)
        }
    },
    2: function(n, t, e) {
        var r = e(30)("wks")
          , o = e(16)
          , i = e(1).Symbol
          , a = "function" == typeof i;
        (n.exports = function(n) {
            return r[n] || (r[n] = a && i[n] || (a ? i : o)("Symbol." + n))
        }
        ).store = r
    },
    20: function(n, t, e) {
        var r = e(34);
        n.exports = function(n, t, e) {
            if (r(n),
            void 0 === t)
                return n;
            switch (e) {
            case 1:
                return function(e) {
                    return n.call(t, e)
                }
                ;
            case 2:
                return function(e, r) {
                    return n.call(t, e, r)
                }
                ;
            case 3:
                return function(e, r, o) {
                    return n.call(t, e, r, o)
                }
            }
            return function() {
                return n.apply(t, arguments)
            }
        }
    },
    202: function(n, t, e) {
        n.exports = e(203)
    },
    203: function(n, t, e) {
        e(43),
        e(43),
        function() {
            "use strict";
            var n = new Date
              , t = (n.getDate(),
            n.getMonth(),
            n.getFullYear(),
            new Date($.now()));
            $.ajaxSettings.async = false;
            var e = [];
            $.post("/calendar/user/", {}, function(data) {
            	e = data;
            },"json");
            var other = [];
            $.post("/calendar/otheruser/", {}, function(data) {
            	other = data;
            },"json");
            //alert(other);
            
            $.ajaxSettings.async = true;

//            var e = [{
//                title: "Hey!",
//                start: new Date($.now() + 158e6),
//                className: "bg-warning"
//            }, {
//                title: "See John Deo",
//                start: t,
//                end: t,
//                className: "bg-success"
//            }, {
//                title: "Meet John Deo",
//                start: new Date($.now() + 168e6),
//                className: "bg-info"
//            }, {
//                title: "Buy a Theme",
//                start: new Date($.now() + 338e6),
//                className: "bg-primary"
//            }];
            $('[data-toggle="fullcalendar"]').each(function() {
                var n = $(this)
                  , t = {
                    themeSystem: "bootstrap4",
                    closeButton: void 0 !== n.data("toastr-close-button") && n.data("toastr-close-button"),
                    slotDuration: "00:15:00",
                    minTime: "08:00:00",
                    maxTime: "19:00:00",
                    defaultView: "month",
                    handleWindowResize: !0,
                    height: $(window).height() - 200,
                    header: {
                        left: "prev,next today",
                        center: "title",
                        right: "month"
                    },
                    events: e,
                    editable: !0,
                    droppable: !0,
                    eventLimit: !0,
                    selectable: !0,
                    drop: function(t) {
                        !function(n, t, e) {
                            var r = n.data("eventObject")
                              , o = n.data("class")
                              , i = $.extend({}, r);
                            i.start = t,
                            o && (i.className = [o]),
                            e.fullCalendar("renderEvent", i, !0),
                            $("#drop-remove").is(":checked") && n.remove()
                        }($(this), t, n)
                    },
                    select: function(t, e, o) {
                        !function(n, t, e, o) {
                        	
                            var i = $(r);
                            i.modal({
                                backdrop: "static"
                            });
                            var sel ='<div class="selectBox" >';
                            sel += '<select class="form-control custom-select" name="others">\n';
//                            other.forEach(function(e){  
//                                 //alert(e['id'] + " " + e['name']);
//                                 sel +='<option value="'+e["id"]+'">'+e["name"]+'</option>\n';
//                            });
                            sel +='<option> Select one</option>\n';
                            sel +='</select>\n';
                            sel +='<div class="overSelect"></div>';
                            sel +='</div>\n';
                            sel +='  <div id="checkboxes">';
                            other.forEach(function(e){  
                            	sel +='<label><input type="checkbox" id="one" value="'+e["id"]+'"/>&nbsp;'+e["name"]+'</label>';
                            });
                            sel +='</div>\n';
                            //alert(sel);
                            var expanded = false;
                            var a = $('<form>\n      <div class="row">\n        <div class="col-12">\n          <div class="form-group">\n            <label class="control-label">Event Name</label>\n            <input class="form-control" placeholder="Insert Event Name" type="text" name="title" />\n          </div>\n        </div>\n  \n        <div class="col-6">\n          <div class="form-group">\n             <label class="control-label">Start Time</label>\n.      <input type="time" class="form-control" name="beginning" value="15:35">\n          </div>\n        </div>\n<div class="col-6">\n          <div class="form-group">\n             <label class="control-label">End Time</label>\n.      <input type="time" name="ending" class="form-control" value="16:35">\n          </div>\n        </div>\n       <div class="col-6">\n          <div class="form-group">\n            <label class="control-label">Category</label>\n            <select class="form-control custom-select" name="category">\n              <option value="bg-danger">Meeting</option>\n              <option value="bg-success">Interview</option>\n              <option value="bg-primary">Appointment</option>\n              <option value="bg-info">Event</option>\n              <option value="bg-dark">Activity</option>\n              <option value="bg-warning">Other</option>\n            </select>\n          </div>\n        </div>\n    <div class="col-6">\n          <div class="form-group">\n            <label class="control-label">Reminder Time</label>\n            <select class="form-control custom-select" name="reminder">\n              <option value="1">1 day before start time</option>\n              <option value="3">3 days before start time</option>\n              <option value="5">5 days before start time</option>\n           </select>\n          </div>\n        </div>\n     <div class="col-12">\n          <div class="form-group">\n            <label class="control-label">Event Details</label>\n            <textarea class="comment" name="description" rows="3" cols="60"> </textarea>\n          </div>\n        </div>\n   <div class="col-12">\n          <div class="form-group">\n            <label class="control-label">Participant</label>\n'.concat(sel,'</div>\n        </div>\n     </div>\n    </form>'));
                            i.find(".delete-event").hide().end().find(".save-event").show().end().find(".modal-body").empty().prepend(a).end().find(".save-event").unbind("click").click(function() {
                                a.submit()
                            }),
                            i.find(".selectBox").on("click", function() {
                            	var checkboxes = document.getElementById("checkboxes");
                                if (!expanded) {
                                  checkboxes.style.display = "block";
                                  expanded = true;
                                } else {
                                  checkboxes.style.display = "none";
                                  expanded = false;
                                }
							}),
                            i.find("form").on("submit", function(e) {
                                e.preventDefault();
                                var r = a.find('input[name="title"]').val()
                                  , b = a.find('input[name="beginning"]').val()
                                  , e = a.find('input[name="ending"]').val()
                                  ,reminder_time = a.find('select[name="reminder"] option:checked').val()
                                  ,description = a.find('textarea[name="description"]').val()
                                  ,c = (a.find('input[name="beginning"]').val(),
                                a.find('input[name="ending"]').val(),
                                a.find('select[name="category"] option:checked').val());
//                                a.find('input[type="checkbox"]:checked').each(function(){
//                                    alert($(this).val()) 
//                                });
                                //var others = a.find('select[name="others"] :selected').val();
                                var others = [];  
                                a.find('input[type="checkbox"]:checked').each(function(){
                                    others.push($(this).val()); 
                                });
//                                a.find('select[name="others"] :selected').each(function(){
//                                    others.push($(this).val()); 
//                                });
                                //alert(others.join());
                                //alert(others);
                                if (null !== r && 0 != r.length){
                                	
  //                               alert(b);
//                                	alert(t.valueOf());
                                	//alert(e);
                                	var starthour = b.split(":")[0],startmin = b.split(":")[1];
                                	var endhour = e.split(":")[0],endmin = e.split(":")[1];
                                	var starttime = n.valueOf()-13*3600000+starthour*3600000+startmin*60000;
                                	
                                	var endtime = n.valueOf()-13*3600000+endhour*3600000+endmin*60000;
                                	var startdate = new Date(starttime);
                                	var enddate = new Date(endtime);

                                	var alertid=0;
                                	var reminder = 0;
                                	//var nowdate = new Date(n.valueOf());
                                	//alert(nowdate)
                                	$.ajaxSettings.async = false;
	                                $.post("/calendar/add/", {title: r,reminder:reminder_time,start: starttime,end: endtime,others:others.join(),allDay: 0,className: c, content: description}, function(data) {
	                                	if(data.code==1){
	                                		alertid = data.id;
	                                		reminder = data.reminder;
	                    					layer.open({
	                    					    content: 'success',
	                    					    btn: 'Got it',
	                    					  });
	                    				}
	                                },"json");
	                                $.ajaxSettings.async = true;
	                                //alert(alertid);
                                    return o.fullCalendar("renderEvent", {
                                    	ifnew: 1,
                                        title: r,
                                        alertid:alertid,
                                        start: startdate,
                                        end: enddate,
                                        allDay: !1,
                                        reminder: reminder,
                                        content: description,
                                        parent_id:null,
                                        className: c
                                    }, !0),
                                    i.modal("hide");
                                }	
                                alert("You have to give a title to your event")
                            }),
                            o.fullCalendar("unselect")
                        }(t, e, 0, n)
                    },
                    eventClick: function(t, e, o) {
                        !function(n, t, e, o) {
//                        	console.log(n);
//                        	alert(n.alertid+"|aId");
//                        	alert(n.parent_id == null? n.alertid:n.parent_id +"|partID")
                        	var participants;
                        	var searchId = n.parent_id == null? n.alertid:n.parent_id;
                        	$.ajaxSettings.async = false;
				            $.post("/calendar/selectedUser/", {searchId:searchId}, function(data) {
				            	participants = data;
				            },"json");
				            $.ajaxSettings.async = true;
				            participants = $.map(participants.ids.split(','), function(value){
							    return parseInt(value, 10);
							    // or return +value; which handles float values as well
							});
                        	var startdate = (n.ifnew==1)?new Date(n.start):new Date(new Date(n.start) - (13*3600000));
                        	var starthours = startdate.getHours(),startmin = startdate.getMinutes();
                        	var enddate = (n.ifnew==1)?new Date(n.end):new Date(new Date(n.end) - (13*3600000)); 
                        	var endhours = enddate.getHours(),endmin = enddate.getMinutes();
                        	starthours = starthours>24?starthours-24:starthours;
                        	endhours = endhours>24?endhours-24:endhours;
                        	var starttime = starthours<10?"0"+starthours+":"+startmin:starthours+":"+startmin;
                        	var endtime = endhours<10?"0"+endhours+":"+endmin:endhours+":"+endmin;
                        	var year = startdate.getFullYear();
                        	var month = (startdate.getMonth()+1)<10?"0"+(startdate.getMonth()+1) : (startdate.getMonth()+1);
                        	var day = startdate.getDate()<10?"0"+startdate.getDate():startdate.getDate();
                        	                        	
//                        	var sel = '<select class="form-control custom-select" name="others" multiple="multiple">\n';
//                            other.forEach(function(e){  
//                                 //alert(e['id'] + " " + e['name']);
////                            	alert(participants.includes(e["id"]))
//                            	var d = participants.includes(e["id"])? 'selected':'';
//                            	var icon = participants.includes(e["id"])? '<div align="rigth"><a href="" class="close" ><span>&times;</span></a></div>':'';
//                                 sel +='<option value="'+e["id"]+'"'+d+'>'+e["name"]+icon+'</option>\n';
//                            });
//                            sel +='</select>\n';
                            
                            var sel ='<div class="selectBox" >';
                            sel += '<select class="form-control custom-select" name="others">\n';
                            sel +='<option> Select one</option>\n';
                            sel +='</select>\n';
                            sel +='<div class="overSelect"></div>';
                            sel +='</div>\n';
                            var permission = n.parent_id!=null;
                            sel +='<div id="checkboxes" style="display:block">';
                            if(permission){
                                other.forEach(function(e){  
                                	var d = participants.includes(e["id"])? 'checked':'';
                                	sel +=participants.includes(e["id"])?'<label><input type="checkbox" id="one" value="'+e["id"]+'"'+d+'/>&nbsp;'+e["name"]+'</label>':"";
                                });
                            }else{
	                            other.forEach(function(e){  
	                            	var d = participants.includes(e["id"])? 'checked':'';
	                            	sel +='<label><input type="checkbox" id="one" value="'+e["id"]+'"'+d+'/>&nbsp;'+e["name"]+'</label>';
	                            });
                            }
                            sel +='</div>\n';
                            var control = permission? 'style="pointer-events: none;"':"";
                            var i = $(r)
                            //  , a = $('<form>\n      <div class="row">\n        <div class="col-12">\n          <div class="form-group">\n            <label class="control-label">Change Event Name</label>\n            <input class="form-control" placeholder="Change Event Name" type="text" name="title" value="'.concat(n.title, '"/>\n          </div>\n        </div>\n  \n        <div class="col-6">\n          <div class="form-group">\n             <label class="control-label">Start Time</label>\n.      <input type="text" class="form-control" name="beginning" value="').concat(starttime,'">\n          </div>\n        </div>\n<div class="col-6">\n          <div class="form-group">\n             <label class="control-label">End Time</label>\n.      <input type="text" name="ending" class="form-control" value="').concat(endtime,'">\n          </div>\n        </div>\n</div>\n       <button type="submit" class="btn btn-success">\n            <i class="material-icons mr-2">check</i> Save\n          </button>    </form>'));
//                              		<form>\n      <div class="row">\n        <div class="col-12">\n          <div class="form-group">\n            <label class="control-label">Event Name</label>\n            <input class="form-control" placeholder="Insert Event Name" type="text" name="title" />\n                                             </div>\n        </div>\n  \n             <div class="col-6">\n          <div class="form-group">\n             <label class="control-label">Start Time</label>\n.      <input type="time" class="form-control" name="beginning" value="15:35">\n          </div>\n        </div>\n<div class="col-6">\n          <div class="form-group">\n             <label class="control-label">End Time</label>\n.      <input type="time" name="ending" class="form-control" value="16:35">\n                         </div>\n        </div>\n       <div class="col-6">\n          <div class="form-group">\n            <label class="control-label">Category</label>\n            <select class="form-control custom-select" name="category">\n              <option value="bg-danger">Meeting</option>\n              <option value="bg-success">Interview</option>\n              <option value="bg-primary">Appointment</option>\n              <option value="bg-info">Event</option>\n              <option value="bg-dark">Activity</option>\n              <option value="bg-warning">Other</option>\n            </select>\n          </div>\n        </div>\n    <div class="col-6">\n          <div class="form-group">\n            <label class="control-label">Reminder Time</label>\n            <select class="form-control custom-select" name="reminder">\n              <option value="1">1 day before start time</option>\n              <option value="3">3 days before start time</option>\n              <option value="5">5 days before start time</option>\n           </select>\n          </div>\n        </div>\n   <div class="col-12">\n          <div class="form-group">\n            <label class="control-label">Participant</label>\n'.concat(sel,'</div>\n        </div>\n     </div>\n    </form>'
                            	, a = $('<form '+control+'>\n      <div class="row">\n        <div class="col-12">\n          <div class="form-group">\n            <label class="control-label">Change Event Name</label>\n            <input class="form-control" placeholder="Change Event Name" type="text" name="title" value="'.concat(n.title, '"/>\n          </div>\n        </div>\n  <div class="col-12">\n          <div class="form-group">\n            <label class="control-label">Event Date:</label>\n <input class="form-control" type="date" name="e_date" value="',year,'-',month,'-',day,'"/> \n </div>\n        </div>\n     <div class="col-6">\n          <div class="form-group">\n             <label class="control-label">Start Time</label>\n.      <input type="time" class="form-control" name="beginning" value="',starttime,'">\n          </div>\n        </div>\n<div class="col-6">\n          <div class="form-group">\n             <label class="control-label">End Time</label>\n.      <input type="time" name="ending" class="form-control" value="',endtime,'">\n          </div>\n        </div>\n       <div class="col-6">\n          <div class="form-group">\n            <label class="control-label">Category</label>\n            <select class="form-control custom-select" name="category">\n              <option value="bg-danger">Meeting</option>\n              <option value="bg-success">Interview</option>\n              <option value="bg-primary">Appointment</option>\n              <option value="bg-info">Event</option>\n              <option value="bg-dark">Activity</option>\n              <option value="bg-warning">Other</option>\n            </select>\n          </div>\n        </div>\n    <div class="col-6">\n          <div class="form-group">\n            <label class="control-label">Reminder Time</label>\n            <select class="form-control custom-select" name="reminder">\n              <option value="1">1 day before start time</option>\n              <option value="3">3 days before start time</option>\n              <option value="5">5 days before start time</option>\n           </select>\n          </div>\n        </div>\n   <div class="col-12">\n          <div class="form-group">\n            <label class="control-label">Event Details</label>\n            <textarea class="comment" name="description" rows="3" cols="60">',n.content == null? '': n.content,'</textarea>\n          </div>\n        </div>\n   <div class="col-12">\n          <div class="form-group">\n            <label class="control-label">Participant</label>\n',sel,'</div>\n        </div>\n  </div>\n       <button type="submit" class="btn btn-success">\n            <i class="material-icons mr-2">check</i> Save\n          </button>    </form>'));
                        	a.find('select[name="category"]').val(n.className).trigger('change');
                        	a.find('select[name="reminder"]').val(n.reminder).trigger('change');
                        	var expanded = true;
                        	i.modal("show");
                        	if (permission) {
                        		i.find(".delete-event").hide().end().find(".save-event").hide().end().find(".modal-body").empty().prepend(a).end().find(".delete-event").unbind("click").click(function() {});
							}else{
	                            i.find(".delete-event").show().end().find(".save-event").hide().end().find(".modal-body").empty().prepend(a).end().find(".delete-event").unbind("click").click(function() {
	                            	$.ajaxSettings.async = false;
	                                $.post("/calendar/del/", {id:n.alertid}, function(data) {
	                                    if(data.code==1){
	                    				}
	                                },"json");
	                                $.ajaxSettings.async = true;
	                            	o.fullCalendar("removeEvents", function(t) {
	                                    return t._id == n._id
	                                }),
	                                i.modal("hide")
	                            });
							}
                            i.find(".selectBox").on("click", function() {
                            	var checkboxes = document.getElementById("checkboxes");
                                if (!expanded) {
                                  checkboxes.style.display = "block";
                                  expanded = true;
                                } else {
                                  checkboxes.style.display = "none";
                                  expanded = false;
                                }
							}),
                            i.find("form").on("submit", function(t) {
                                t.preventDefault();
                                var r = a.find('input[name="title"]').val()
                                ,y = a.find('input[name="e_date"]').val()
                                ,b = a.find('input[name="beginning"]').val()
                                ,e = a.find('input[name="ending"]').val()
                                ,c = (a.find('input[name="beginning"]').val(),
                                        a.find('input[name="ending"]').val(),
                                        a.find('select[name="category"] option:checked').val());
                                var reminder = a.find('select[name="reminder"] option:checked').val();
                                n.reminder = reminder;
                                var content = a.find('textarea[name="description"]').val();
                                n.content = content;
                                var others = [];  
                                a.find('input[type="checkbox"]:checked').each(function(){
                                    others.push($(this).val()); 
                                });
//                                var b = a.find('input[name="beginning"]').val();
//                                var e = a.find('input[name="ending"]').val();
//                                
//                                var startdate = new Date(n.start);
//                                var thedate = startdate.getFullYear()+'-'+(startdate.getMonth()+1)+'-'+startdate.getDate();
//                                var begindate = new Date(startdate.toDateString());
//                                //alert(begindate)
//                                //alert(n.valueOf())
//                                var starthour = b.split(":")[0],startmin = b.split(":")[1];
//                            	var endhour = e.split(":")[0],endmin = e.split(":")[1];
//                            	var starttime = begindate.valueOf()+starthour*3600000+startmin*60000;
//                            	var endtime = begindate.valueOf()+endhour*3600000+endmin*60000;
//                            	var startdate = new Date(starttime);
//                            	var enddate = new Date(endtime);
//                            	if(n.ifnew==1){
//                            		
//                            	}
//                            	n.start = startdate;
//                            	n.end = enddate;
                            	//alert(thedate);
                                if (null !== r && 0 != r.length){
                                	var starthour = b.split(":")[0],startmin = b.split(":")[1];
                                	var endhour = e.split(":")[0],endmin = e.split(":")[1];
                                	var n_date = new Date(y);
                                	var n_start = new Date(new Date(startdate.setHours(starthour,startmin)) +(13*3600000));
                                	var n_end = new Date(new Date(enddate.setHours(endhour,endmin)) + (13*3600000));
                                	var u_start = new Date(n_date.getFullYear(),n_date.getMonth(),n_date.getDate(),n_start.getHours(),n_start.getMinutes());
                                	var u_end = new Date(n_date.getFullYear(),n_date.getMonth(),n_date.getDate(),n_end.getHours(),n_end.getMinutes());
                                	n.start = u_start;
                                	n.end = u_end;
//                                	n.end = n_end.setFullYear(n_date.getFullYear()).setMonth(n_date.getMonth()).setDate(n_date.getDate());
                                	var starttime = n.start.getTime();
                                	var endtime = n.end.getTime();
                                	n.title = r;
                                	n.className[0] = c;
                                	$.ajaxSettings.async = false;
	                                //$.post("/calendar/update/", {id:n.alertid,title: n.title,start: starttime,end: endtime}, function(data) {
	                                $.post("/calendar/update/", {id:n.alertid,title: r,reminder:reminder,start: starttime,end: endtime,others:others.join(),allDay: 0,className: c, content: content, parent_id: n.parent_id}, function(data) {
	                                        	if(data.code==1){
	                                		//alertid = data.id;
	//                    					layer.open({
	//                    					    content: 'success',
	//                    					    btn: 'Got it',
	//                    					  });
	                    				}
	                                },"json");
	                                $.ajaxSettings.async = true;
	                                
	                                o.fullCalendar("updateEvent", n),
	                                i.modal("hide")
                                }else{
                                	alert("You have to give a title to your event")
                                }
                            })
                        }(t, 0, 0, n)
                    }
                };
                n.fullCalendar(t)
            });
            var r = "#event-modal"
              , o = $("#add-category form")
              , i = function() {
                $("#external-events div.external-event").each(function() {
                    $(this).data("eventObject") || ($(this).data("eventObject", {
                        title: $.trim($(this).find(".external-event__title").text())
                    }),
                    $(this).draggable({
                        zIndex: 999,
                        revert: !0,
                        revertDuration: 0
                    }))
                })
            };
            i(),
            $(".save-category").on("click", function() {
                var n = o.find('input[name="category-name"]').val()
                  , t = o.find('select[name="category-color"]').val();
                null !== n && 0 != n.length && ($("#external-events").append('\n        <div class="external-event bg-'.concat(t, '" data-class="bg-').concat(t, '">\n          <i class="mr-2 material-icons">drag_handle</i>\n          <span class="external-event__title">').concat(n, "</span>\n        </div>\n      ")),
                i())
            })
        }()
    },
    21: function(n, t, e) {
        var r = e(24)
          , o = Math.min;
        n.exports = function(n) {
            return n > 0 ? o(r(n), 9007199254740991) : 0
        }
    },
    23: function(n, t) {
        n.exports = function(n, t) {
            return {
                enumerable: !(1 & n),
                configurable: !(2 & n),
                writable: !(4 & n),
                value: t
            }
        }
    },
    24: function(n, t) {
        var e = Math.ceil
          , r = Math.floor;
        n.exports = function(n) {
            return isNaN(n = +n) ? 0 : (n > 0 ? r : e)(n)
        }
    },
    25: function(n, t, e) {
        var r = e(17);
        n.exports = function(n) {
            return Object(r(n))
        }
    },
    26: function(n, t) {
        n.exports = !1
    },
    3: function(n, t) {
        n.exports = function(n) {
            return "object" == typeof n ? null !== n : "function" == typeof n
        }
    },
    30: function(n, t, e) {
        var r = e(13)
          , o = e(1)
          , i = o["__core-js_shared__"] || (o["__core-js_shared__"] = {});
        (n.exports = function(n, t) {
            return i[n] || (i[n] = void 0 !== t ? t : {})
        }
        )("versions", []).push({
            version: r.version,
            mode: e(26) ? "pure" : "global",
            copyright: "© 2018 Denis Pushkarev (zloirock.ru)"
        })
    },
    31: function(n, t, e) {
        var r = e(3)
          , o = e(1).document
          , i = r(o) && r(o.createElement);
        n.exports = function(n) {
            return i ? o.createElement(n) : {}
        }
    },
    33: function(n, t, e) {
        var r = e(3);
        n.exports = function(n, t) {
            if (!r(n))
                return n;
            var e, o;
            if (t && "function" == typeof (e = n.toString) && !r(o = e.call(n)))
                return o;
            if ("function" == typeof (e = n.valueOf) && !r(o = e.call(n)))
                return o;
            if (!t && "function" == typeof (e = n.toString) && !r(o = e.call(n)))
                return o;
            throw TypeError("Can't convert object to primitive value")
        }
    },
    34: function(n, t) {
        n.exports = function(n) {
            if ("function" != typeof n)
                throw TypeError(n + " is not a function!");
            return n
        }
    },
    36: function(n, t, e) {
        var r = e(2)("unscopables")
          , o = Array.prototype;
        null == o[r] && e(4)(o, r, {}),
        n.exports = function(n) {
            o[r][n] = !0
        }
    },
    37: function(n, t, e) {
        var r = e(19);
        n.exports = Object("z").propertyIsEnumerable(0) ? Object : function(n) {
            return "String" == r(n) ? n.split("") : Object(n)
        }
    },
    4: function(n, t, e) {
        var r = e(9)
          , o = e(23);
        n.exports = e(6) ? function(n, t, e) {
            return r.f(n, t, o(1, e))
        }
        : function(n, t, e) {
            return n[t] = e,
            n
        }
    },
    40: function(n, t, e) {
        n.exports = !e(6) && !e(8)(function() {
            return 7 != Object.defineProperty(e(31)("div"), "a", {
                get: function() {
                    return 7
                }
            }).a
        })
    },
    43: function(n, t, e) {
        "use strict";
        var r = e(11)
          , o = e(62)(5)
          , i = !0;
        "find"in [] && Array(1).find(function() {
            i = !1
        }),
        r(r.P + r.F * i, "Array", {
            find: function(n) {
                return o(this, n, arguments.length > 1 ? arguments[1] : void 0)
            }
        }),
        e(36)("find")
    },
    56: function(n, t, e) {
        var r = e(19);
        n.exports = Array.isArray || function(n) {
            return "Array" == r(n)
        }
    },
    6: function(n, t, e) {
        n.exports = !e(8)(function() {
            return 7 != Object.defineProperty({}, "a", {
                get: function() {
                    return 7
                }
            }).a
        })
    },
    62: function(n, t, e) {
        var r = e(20)
          , o = e(37)
          , i = e(25)
          , a = e(21)
          , c = e(73);
        n.exports = function(n, t) {
            var e = 1 == n
              , u = 2 == n
              , l = 3 == n
              , f = 4 == n
              , s = 6 == n
              , d = 5 == n || s
              , p = t || c;
            return function(t, c, v) {
                for (var m, y, b = i(t), g = o(b), h = r(c, v, 3), x = a(g.length), w = 0, _ = e ? p(t, x) : u ? p(t, 0) : void 0; x > w; w++)
                    if ((d || w in g) && (y = h(m = g[w], w, b),
                    n))
                        if (e)
                            _[w] = y;
                        else if (y)
                            switch (n) {
                            case 3:
                                return !0;
                            case 5:
                                return m;
                            case 6:
                                return w;
                            case 2:
                                _.push(m)
                            }
                        else if (f)
                            return !1;
                return s ? -1 : l || f ? f : _
            }
        }
    },
    7: function(n, t, e) {
        var r = e(3);
        n.exports = function(n) {
            if (!r(n))
                throw TypeError(n + " is not an object!");
            return n
        }
    },
    73: function(n, t, e) {
        var r = e(74);
        n.exports = function(n, t) {
            return new (r(n))(t)
        }
    },
    74: function(n, t, e) {
        var r = e(3)
          , o = e(56)
          , i = e(2)("species");
        n.exports = function(n) {
            var t;
            return o(n) && ("function" != typeof (t = n.constructor) || t !== Array && !o(t.prototype) || (t = void 0),
            r(t) && null === (t = t[i]) && (t = void 0)),
            void 0 === t ? Array : t
        }
    },
    8: function(n, t) {
        n.exports = function(n) {
            try {
                return !!n()
            } catch (n) {
                return !0
            }
        }
    },
    9: function(n, t, e) {
        var r = e(7)
          , o = e(40)
          , i = e(33)
          , a = Object.defineProperty;
        t.f = e(6) ? Object.defineProperty : function(n, t, e) {
            if (r(n),
            t = i(t, !0),
            r(e),
            o)
                try {
                    return a(n, t, e)
                } catch (n) {}
            if ("get"in e || "set"in e)
                throw TypeError("Accessors not supported!");
            return "value"in e && (n[t] = e.value),
            n
        }
    }
});
