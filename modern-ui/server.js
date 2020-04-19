const express = require('express');
const app = express(),
      bodyParser = require("body-parser");

var port = process.env.PORT || 4200;

var bffURL = process.env.BFF_URL || "http://localhost:8080";

app.disable('x-powered-by');

app.use(bodyParser.json());

app.use(express.static(process.cwd()+"/public/my-app/"));

app.get('/', (req,res) => {
    res.sendFile(process.cwd()+"/public/my-app/index.html")
});

app.get('/app-config',(req,res) => {
	res.json({bffUrl: bffURL});
});

app.get('/health',(req,res) => {
	res.json({status: 'UP'});
});

app.use((req, res, next)=>{
  
    // respond with index html page
    if (req.accepts('html')) {
        res.sendFile(process.cwd()+"/public/my-app/index.html")
        return;
    }

    //404s will fallback to //index.html    
    res.status(404);

    // respond with json
    if (req.accepts('json')) {
      res.send({ error: 'Not found' });
      return;
    }
  
    // default to plain-text. send()
    res.type('txt').send('Not found');
});

app.listen(port, () => {
    console.log(`Server listening on the port::${port}`);
});