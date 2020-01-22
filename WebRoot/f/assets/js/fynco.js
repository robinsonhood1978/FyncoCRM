function ValidateEmail(mail)  {
  	 if (/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/.test(mail))
	  {
	    return (true)
	  }
	  layer.open({
	    content: 'You have entered an invalid email address!',
	    btn: 'Got it'
		  });
      return (false)
  }
