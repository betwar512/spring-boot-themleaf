  	
$(document).ready(
    $(function () {
    var dateNow=new Date();
     var date = new Date();
    date.setDate(date.getDate() - 1);
        $('#datetimepickermin').datetimepicker({
        	 format : 'DD/MM/YYYY HH:mm',
    		 defaultDate:moment(date).hours(7).minutes(0).seconds(0).milliseconds(0)
        });
        
        $('#datetimepickermax').datetimepicker({
        	 format : 'DD/MM/YYYY HH:mm',
        		 defaultDate:moment(dateNow).hours(7).minutes(0).seconds(0).milliseconds(0)
        }); 
    }));
