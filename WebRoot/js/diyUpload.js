(function($){$.fn.extend({diyUpload:function(opt,serverCallBack){if(typeof opt!="object"){alert('Parameter error!');return;}
var $fileInput=$(this);var $fileInputId=$fileInput.attr('id');if(opt.url){opt.server=opt.url;delete opt.url;}
if(opt.success){var successCallBack=opt.success;delete opt.success;}
if(opt.error){var errorCallBack=opt.error;delete opt.error;}
$.each(getOption('#'+$fileInputId),function(key,value){opt[key]=opt[key]||value;});if(opt.buttonText){opt['pick']['label']=opt.buttonText;delete opt.buttonText;}
var webUploader=getUploader(opt);if(!WebUploader.Uploader.support()){alert(' The upload component does not support your browser!');return false;}
webUploader.on('fileQueued',function(file){
	createBox($fileInput,file,webUploader);
	
	});
webUploader.on('filesQueued',function(file){
	var classification = document.getElementsByName("classification");
	for(var i=0;i<classification.length;i++){
		classification[i].innerHTML='<select name="classify">\
			<option value="Application Form">Application Form</option>\
			<option value="Approval Letter">Approval Letter</option>\
			<option value="Appraisal">Appraisal</option>\
			<option value="Gifting Letter">Gifting Letter</option>\
			<option value="Proof of Identity">Proof of Identity</option>\
			<option value="Rate Lock Form">Rate Lock Form</option>\
			<option value="Sales Purchase Agreement">Sales Purchase Agreement</option>\
			<option value="IRD Summary">IRD Summary</option>\
			<option value="Payslip">Payslip</option>\
			<option value="Boarder Agreement">Boarder Agreement</option>\
			<option value="Valuation Report">Valuation Report</option>\
			<option value="Loan Statement">Loan Statement</option>\
			<option value="Financial Report">Financial Report</option>\
			<option value="Lease Agreement">Lease Agreement</option>\
			<option value="Building Report">Building Report</option>\
			<option value="Loan agreement">Loan agreement</option>\
			<option value="Bank Statement">Bank Statement</option>\
			<option value="Rental Agreement">Rental Agreement</option>\
			<option value="Support Letter">Support Letter</option>\
			<option value="Additional Files">Additional Files</option>\
			</select>';
	}
	$('#pdf_num').val(classification.length);
});
webUploader.on('uploadProgress',function(file,percentage){var $fileBox=$('#fileBox_'+file.id);var $diyBar=$fileBox.find('.diyBar');$diyBar.show();percentage=percentage*100;showDiyProgress(percentage.toFixed(2),$diyBar);});webUploader.on('uploadFinished',function(){$fileInput.next('.parentFileBox').children('.diyButton').remove();});webUploader.on('uploadAccept',function(object,data){if(serverCallBack)serverCallBack(data);});webUploader.on('uploadSuccess',function(file,response){var $fileBox=$('#fileBox_'+file.id);var $diyBar=$fileBox.find('.diyBar');$fileBox.removeClass('diyUploadHover');$diyBar.fadeOut(1000,function(){$fileBox.children('.diySuccess').show();});if(successCallBack){successCallBack(response);}});webUploader.on('uploadError',function(file,reason){var $fileBox=$('#fileBox_'+file.id);var $diyBar=$fileBox.find('.diyBar');showDiyProgress(0,$diyBar,'Upload failed!');var err='Upload failed! The file:'+file.name+' error code:'+reason;if(errorCallBack){errorCallBack(err);}});webUploader.on('error',function(code){var text='';switch(code){case 'F_DUPLICATE':text='The file has been selected!';break;case 'Q_EXCEED_NUM_LIMIT':text='The number of uploaded files exceeds the limit!';break;case 'F_EXCEED_SIZE':text='File size exceeds limit!';break;case 'Q_EXCEED_SIZE_LIMIT':text='Total file size exceeds limit!';break;case 'Q_TYPE_DENIED':text='Incorrect file type or empty file!';break;default:text='Unknown mistake!';break;}
alert(text);});}});function getOption(objId){return{pick:{id:objId,label:"Click to select file"},accept:{title:"Images",extensions:"gif,jpg,jpeg,bmp,png",mimeTypes:"image/*"},thumb:{width:170,height:150,quality:70,allowMagnify:false,crop:true,type:"image/jpeg"},method:"POST",server:"",sendAsBinary:false,chunked:false,chunkSize:512*1024,fileNumLimit:50,fileSizeLimit:5000*1024,fileSingleSizeLimit:500*1024};}
function getUploader(opt){return new WebUploader.Uploader(opt);;}
function showDiyProgress(progress,$diyBar,text){if(progress>=100){progress=progress+'%';text=text||'Upload completed';}else{progress=progress+'%';text=text||progress;}
var $diyProgress=$diyBar.find('.diyProgress');var $diyProgressText=$diyBar.find('.diyProgressText');$diyProgress.width(progress);$diyProgressText.text(text);}
function removeLi($li,file_id,webUploader){webUploader.removeFile(file_id);if($li.siblings('li').length<=0){$li.parents('.parentFileBox').remove();}else{$li.remove();}}
function createBox($fileInput,file,webUploader){var file_id=file.id;var $parentFileBox=$fileInput.next('.parentFileBox');if($parentFileBox.length<=0){var div='<div class="parentFileBox"> \
						<ul class="fileBoxUl"></ul>\
					</div>';$fileInput.after(div);$parentFileBox=$fileInput.next('.parentFileBox');}
if($parentFileBox.find('.diyButton').length<=0){var div='<div class="diyButton"> \
						<a class="diyStart" href="javascript:void(0)">Start Uploading</a> \
						<a class="diyCancelAll" href="javascript:void(0)">All Cancelled</a> \
	</div>';$parentFileBox.append(div);var $startButton=$parentFileBox.find('.diyStart');var $cancelButton=$parentFileBox.find('.diyCancelAll');var uploadStart=function(){webUploader.upload();$startButton.text('Pause Uploading').one('click',function(){webUploader.stop();$(this).text('Continue Uploading').one('click',function(){uploadStart();});});}
$startButton.one('click',uploadStart);
$cancelButton.bind('click',function(){var fileArr=webUploader.getFiles('queued');$.each(fileArr,function(i,v){removeLi($('#fileBox_'+v.id),v.id,webUploader);});});}
var li='<li id="fileBox_'+file_id+'" class="diyUploadHover"> \
					<div class="diyUnit"><div class="diyFileName">'+file.name+'<input type="hidden" name="file_name" value="'+file.name+'"></div><div name="diyFileSize" class="diyFileSize">'+conver(file.size)+'<input type="hidden" name="file_size" value="'+file.size+'"></div><div name="classification" class="classification"></div></div>\
					<div class="diyCancel"></div> \
					<div class="diySuccess"></div> \
					<div class="diyBar"> \
							<div class="diyProgress"></div> \
							<div class="diyProgressText">0%</div> \
					</div> \
				</li>';$parentFileBox.children('.fileBoxUl').append(li);var $width=$('.fileBoxUl>li').length*180;var $maxWidth=$fileInput.parent().width();$width=$maxWidth>$width?$width:$maxWidth;$parentFileBox.width('100%');var $fileBox=$parentFileBox.find('#fileBox_'+file_id);var $diyCancel=$fileBox.children('.diyCancel').one('click',function(){removeLi($(this).parent('li'),file_id,webUploader);});if(file.type.split("/")[0]!='image'){var liClassName=getFileTypeClassName(file.name.split(".").pop());$fileBox.addClass(liClassName);
				return;}
webUploader.makeThumb(file,function(error,dataSrc){if(!error){$fileBox.find('.viewThumb').append('<img src="'+dataSrc+'" >');}});}
function getFileTypeClassName(type){var fileType={};var suffix='_diy_bg';fileType['pdf']='pdf';fileType['zip']='zip';fileType['rar']='rar';fileType['csv']='csv';fileType['doc']='doc';fileType['xls']='xls';fileType['xlsx']='xls';fileType['txt']='txt';fileType=fileType[type]||'txt';return fileType+suffix;}})(jQuery);