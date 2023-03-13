// TODO: onload function should retrieve the data needed to populate the UI


window.onload = function() {
  fetch("http://localhost:8080/spamDetector-1.0/api/spam",{
    mode: 'cors',
  })
    .then((response) => {
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      return response.json();
    })

    .then(data => {


      //add the data in tbody
      const body = document.querySelector("#chart tbody");
      console.log(data);
      data.forEach((info) => {

          const row = body.insertRow(-1);
          const File = row.insertCell(0);
          const Spam_Probability = row.insertCell(1);
          const Class = row.insertCell(2);

          //set the values inside the tbody

          File.innerHTML =info.filename;
            Spam_Probability.innerHTML =info.spamProbability;
              Class.innerHTML =info.actualClass;


        });
    })


    .catch((err) => {
      console.log("Something went wrong: " + err);
    });




}


