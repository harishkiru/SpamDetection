window.addEventListener("load", () => {
  const loader = document.querySelector(".loader");
  const thead = document.querySelector('.tableFixHead thead');

  fetch("http://localhost:8080/spamDetector-1.0/api/spam/accuracy", {
    mode: "cors",
  })
    .then((response) => {
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      return response.json();
    })
    .then((data) => {
      // add the data to accuracyText

      document.querySelector('#accuracyText').value = data.accuracy;
    })
    .catch((err) => {
      console.log("Something went wrong: " + err);
    });

  fetch("http://localhost:8080/spamDetector-1.0/api/spam/precision", {
    mode: "cors",
  })
    .then((response) => {
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      return response.json();
    })
    .then((data) => {
      // add the data to accuracyText
      console.log(data.precision);
      document.querySelector('#precisionText').value = data.precision;
    })
    .catch((err) => {
      console.log("Something went wrong: " + err);
    });

  fetch("http://localhost:8080/spamDetector-1.0/api/spam", {
    mode: "cors",
  })
    .then((response) => {
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      return response.json();
    })
    .then((data) => {
      // add the data in tbody
      const body = document.querySelector("#chart tbody");
      console.log(data);
      data.forEach((info) => {
        const row = body.insertRow(-1);
        const File = row.insertCell(0);
        const Spam_Probability = row.insertCell(1);
        const Class = row.insertCell(2);

        // set the values inside the tbody
        File.innerHTML = info.filename;
        Spam_Probability.innerHTML = info.spamProbability;
        Class.innerHTML = info.actualClass;
      });

      loader.classList.add("loader--hidden");

      loader.addEventListener("transitionend", () => {
        document.body.removeChild(loader);
      });
      thead.style.display = 'table-header-group';

    })
    .catch((err) => {
      console.log("Something went wrong: " + err);
    });
});
