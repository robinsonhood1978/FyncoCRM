(function($){$.fn.extend({diyUpload:function(opt,serverCallBack){if(typeof opt!="object"){alert('参数错误!');return;}
var $fileInput=$(this);var $fileInputId=$fileInput.attr('id');if(opt.url){opt.server=opt.url;delete opt.url;}
if(opt.success){var successCallBack=opt.success;delete opt.success;}
if(opt.error){var errorCallBack=opt.error;delete opt.error;}
$.each(getOption('#'+$fileInputId),function(key,value){opt[key]=opt[key]||value;});if(opt.buttonText){opt['pick']['label']=opt.buttonText;delete opt.buttonText;}
var webUploader=getUploader(opt);if(!WebUploader.Uploader.support()){alert(' 上传组件不支持您的浏览器！');return false;}
webUploader.on('fileQueued',function(file){createBox($fileInput,file,webUploader);});webUploader.on('uploadProgress',function(file,percentage){var $fileBox=$('#fileBox_'+file.id);var $diyBar=$fileBox.find('.diyBar');$diyBar.show();percentage=percentage*100;showDiyProgress(percentage.toFixed(2),$diyBar);});webUploader.on('uploadFinished',function(){$fileInput.next('.parentFileBox').children('.diyButton').remove();});webUploader.on('uploadAccept',function(object,data){if(serverCallBack)serverCallBack(data);});webUploader.on('uploadSuccess',function(file,response){var $fileBox=$('#fileBox_'+file.id);var $diyBar=$fileBox.find('.diyBar');$fileBox.removeClass('diyUploadHover');$diyBar.fadeOut(1000,function(){$fileBox.children('.diySuccess').show();});if(successCallBack){successCallBack(response);}});webUploader.on('uploadError',function(file,reason){var $fileBox=$('#fileBox_'+file.id);var $diyBar=$fileBox.find('.diyBar');showDiyProgress(0,$diyBar,'上传失败!');var err='上传失败! 文件:'+file.name+' 错误码:'+reason;if(errorCallBack){errorCallBack(err);}});webUploader.on('error',function(code){var text='';switch(code){case 'F_DUPLICATE':text='该文件已经被选择了!';break;case 'Q_EXCEED_NUM_LIMIT':text='上传文件数量超过限制!';break;case 'F_EXCEED_SIZE':text='文件大小超过限制!';break;case 'Q_EXCEED_SIZE_LIMIT':text='所有文件总大小超过限制!';break;case 'Q_TYPE_DENIED':text='文件类型不正确或者是空文件!';break;default:text='未知错误!';break;}
alert(text);});}});function getOption(objId){return{pick:{id:objId,label:"点击选择图片"},accept:{title:"Images",extensions:"gif,jpg,jpeg,bmp,png",mimeTypes:"image/*"},thumb:{width:170,height:150,quality:70,allowMagnify:false,crop:true,type:"image/jpeg"},method:"POST",server:"",sendAsBinary:false,chunked:true,chunkSize:512*1024,fileNumLimit:50,fileSizeLimit:5000*1024,fileSingleSizeLimit:500*1024};}
function getUploader(opt){return new WebUploader.Uploader(opt);;}
function showDiyProgress(progress,$diyBar,text){if(progress>=100){progress=progress+'%';text=text||'上传完成';}else{progress=progress+'%';text=text||progress;}
var $diyProgress=$diyBar.find('.diyProgress');var $diyProgressText=$diyBar.find('.diyProgressText');$diyProgress.width(progress);$diyProgressText.text(text);}
function removeLi($li,file_id,webUploader){webUploader.removeFile(file_id);if($li.siblings('li').length<=0){$li.parents('.parentFileBox').remove();}else{$li.remove();}}
function createBox($fileInput,file,webUploader){var file_id=file.id;var $parentFileBox=$fileInput.next('.parentFileBox');if($parentFileBox.length<=0){var div='<div class="parentFileBox"> \
						<ul class="fileBoxUl"></ul>\
					</div>';$fileInput.after(div);$parentFileBox=$fileInput.next('.parentFileBox');}
if($parentFileBox.find('.diyButton').length<=0){var div='<div class="diyButton"> \
						<a class="diyStart" href="javascript:void(0)">开始上传</a> \
						<a class="diyCancelAll" href="javascript:void(0)">全部取消</a> \
					</div>';$parentFileBox.append(div);var $startButton=$parentFileBox.find('.diyStart');var $cancelButton=$parentFileBox.find('.diyCancelAll');var uploadStart=function(){webUploader.upload();$startButton.text('暂停上传').one('click',function(){webUploader.stop();$(this).text('继续上传').one('click',function(){uploadStart();});});}
$startButton.one('click',uploadStart);$cancelButton.bind('click',function(){var fileArr=webUploader.getFiles('queued');$.each(fileArr,function(i,v){removeLi($('#fileBox_'+v.id),v.id,webUploader);});});}
var li='<li id="fileBox_'+file_id+'" class="diyUploadHover"> \
					<div class="viewThumb"></div> \
					<div class="diyCancel"></div> \
					<div class="diySuccess"></div> \
					<div class="diyFileName">'+file.name+'</div>\
					<div class="diyBar"> \
							<div class="diyProgress"></div> \
							<div class="diyProgressText">0%</div> \
					</div> \
				</li>';$parentFileBox.children('.fileBoxUl').append(li);var $width=$('.fileBoxUl>li').length*180;var $maxWidth=$fileInput.parent().width();$width=$maxWidth>$width?$width:$maxWidth;$parentFileBox.width($width);var $fileBox=$parentFileBox.find('#fileBox_'+file_id);var $diyCancel=$fileBox.children('.diyCancel').one('click',function(){removeLi($(this).parent('li'),file_id,webUploader);});if(file.type.split("/")[0]!='image'){var liClassName=getFileTypeClassName(file.name.split(".").pop());$fileBox.addClass(liClassName);return;}
webUploader.makeThumb(file,function(error,dataSrc){if(!error){$fileBox.find('.viewThumb').append('<img src="'+dataSrc+'" >');}});}
function getFileTypeClassName(type){var fileType={};var suffix='_diy_bg';fileType['pdf']='pdf';fileType['zip']='zip';fileType['rar']='rar';fileType['csv']='csv';fileType['doc']='doc';fileType['xls']='xls';fileType['xlsx']='xls';fileType['txt']='txt';fileType=fileType[type]||'txt';return fileType+suffix;}})(jQuery);