document.addEventListener('DOMContentLoaded', function() {
  // Get elements
  var showNavigatorButton = document.getElementById('showNavigatorButton');
  var hideNavigatorButton = document.getElementById('hideNavigatorButton');
  var navigator = document.getElementById('navigator');
  var summariesDetails = document.getElementById('summariesDetails');

  // Function to show navigator
  function showNavigator() {
    navigator.classList.remove('d-none');
    navigator.classList.add('d-block');
    
    // Calculate classes based on conditions
    var summaryClasses = summariesDetails.classList;
    var newSummaryClass = getNewSummaryClass(summaryClasses);
    if (newSummaryClass) {
      // Calculate the new class for summariesDetails
      var navigatorClassValue = parseInt(getNumericClass(navigator.classList));
      if (!isNaN(navigatorClassValue)) {
        var newSummariesDetailsClassValue = 12 - navigatorClassValue;
        summariesDetails.classList.remove(newSummaryClass);
        summariesDetails.classList.add('ui-g-' + newSummariesDetailsClassValue);
      }
    }
    
    // Toggle button visibility
    showNavigatorButton.style.display = 'none';
    hideNavigatorButton.style.display = 'inline-block';

    // Save current state in localStorage
    localStorage.setItem('navigatorClass', navigator.className);
    localStorage.setItem('summariesDetailsClass', summariesDetails.className);
    localStorage.setItem('showNavigatorButtonDisplay', showNavigatorButton.style.display);
    localStorage.setItem('hideNavigatorButtonDisplay', hideNavigatorButton.style.display);
  }

  // Function to hide navigator
  function hideNavigator() {
    navigator.classList.remove('d-block');
    navigator.classList.add('d-none');
    
    // Calculate classes based on conditions
    var summaryClasses = summariesDetails.classList;
    var newSummaryClass = getNewSummaryClass(summaryClasses);
    if (newSummaryClass) {
      // Calculate the new class for summariesDetails
      summariesDetails.classList.remove(newSummaryClass);
      summariesDetails.classList.add('ui-g-12');
    }
    
    // Toggle button visibility
    showNavigatorButton.style.display = 'inline-block';
    hideNavigatorButton.style.display = 'none';

    // Save current state in localStorage
    localStorage.setItem('navigatorClass', navigator.className);
    localStorage.setItem('summariesDetailsClass', summariesDetails.className);
    localStorage.setItem('showNavigatorButtonDisplay', showNavigatorButton.style.display);
    localStorage.setItem('hideNavigatorButtonDisplay', hideNavigatorButton.style.display);
  }

  // Function to get the class to be removed from summariesDetails
  function getNewSummaryClass(classes) {
    var newClass = '';
    for (var i = 0; i < classes.length; i++) {
      if (classes[i].startsWith('ui-g-')) {
        newClass = classes[i];
        break;
      }
    }
    return newClass;
  }

  // Function to get the numerical value of a class
  function getNumericClass(classList) {
    for (var i = 0; i < classList.length; i++) {
      var classValue = parseInt(classList[i].split('-')[2]);
      if (!isNaN(classValue)) {
        return classValue;
      }
    }
    return NaN;
  }

  // Restore the state from localStorage
  var navigatorClass = localStorage.getItem('navigatorClass');
  var summariesDetailsClass = localStorage.getItem('summariesDetailsClass');
  var showNavigatorButtonDisplay = localStorage.getItem('showNavigatorButtonDisplay');
  var hideNavigatorButtonDisplay = localStorage.getItem('hideNavigatorButtonDisplay');

  if (navigatorClass) {
    navigator.className = navigatorClass;
  }
  if (summariesDetailsClass) {
    summariesDetails.className = summariesDetailsClass;
  }
  if (showNavigatorButtonDisplay) {
    showNavigatorButton.style.display = showNavigatorButtonDisplay;
  }
  if (hideNavigatorButtonDisplay) {
    hideNavigatorButton.style.display = hideNavigatorButtonDisplay;
  } else {
    hideNavigatorButton.style.display = 'none'; // Initial state
  }

  showNavigator();

  // Add event listeners to the buttons
  showNavigatorButton.addEventListener('click', function(event) {
    event.preventDefault();
    showNavigator();
  });

  hideNavigatorButton.addEventListener('click', function(event) {
    event.preventDefault();
    hideNavigator();
  });

  // Show tooltips on page load
  showNavigatorButton.classList.add('show-tooltip');
  hideNavigatorButton.classList.add('show-tooltip');

  // Hide tooltips after a few seconds
  setTimeout(function() {
    showNavigatorButton.classList.remove('show-tooltip');
    hideNavigatorButton.classList.remove('show-tooltip');
  }, 20000); // Adjust the duration as needed (20000ms = 20 seconds)
});




// document.addEventListener('DOMContentLoaded', function() {
//   // Get elements
//   var showNavigatorButton = document.getElementById('showNavigatorButton');
//   var hideNavigatorButton = document.getElementById('hideNavigatorButton');
//   var navigator = document.getElementById('navigator');
//   var summariesDetails = document.getElementById('summariesDetails');

//   // Function to show navigator
//   function showNavigator() {
//       navigator.classList.remove('d-none');
//       navigator.classList.add('d-block');
//       summariesDetails.classList.remove('ui-g-12');
//       summariesDetails.classList.add('ui-g-9');

//       // Toggle button visibility
//       showNavigatorButton.style.display = 'none';
//       hideNavigatorButton.style.display = 'inline-block';

//       // Save current state in localStorage
//       localStorage.setItem('navigatorClass', navigator.className);
//       localStorage.setItem('summariesDetailsClass', summariesDetails.className);
//       localStorage.setItem('showNavigatorButtonDisplay', showNavigatorButton.style.display);
//       localStorage.setItem('hideNavigatorButtonDisplay', hideNavigatorButton.style.display);
//   }

//   // Function to hide navigator
//   function hideNavigator() {
//       navigator.classList.remove('d-block');
//       navigator.classList.add('d-none');
//       summariesDetails.classList.remove('ui-g-9');
//       summariesDetails.classList.add('ui-g-12');

//       // Toggle button visibility
//       showNavigatorButton.style.display = 'inline-block';
//       hideNavigatorButton.style.display = 'none';

//       // Save current state in localStorage
//       localStorage.setItem('navigatorClass', navigator.className);
//       localStorage.setItem('summariesDetailsClass', summariesDetails.className);
//       localStorage.setItem('showNavigatorButtonDisplay', showNavigatorButton.style.display);
//       localStorage.setItem('hideNavigatorButtonDisplay', hideNavigatorButton.style.display);
//   }

//   // Restore the state from localStorage
//   var navigatorClass = localStorage.getItem('navigatorClass');
//   var summariesDetailsClass = localStorage.getItem('summariesDetailsClass');
//   var showNavigatorButtonDisplay = localStorage.getItem('showNavigatorButtonDisplay');
//   var hideNavigatorButtonDisplay = localStorage.getItem('hideNavigatorButtonDisplay');

//   if (navigatorClass) {
//     navigator.className = navigatorClass;
//   }
//   if (summariesDetailsClass) {
//     summariesDetails.className = summariesDetailsClass;
//   }
//   if (showNavigatorButtonDisplay) {
//     showNavigatorButton.style.display = showNavigatorButtonDisplay;
//   }
//   if (hideNavigatorButtonDisplay) {
//     hideNavigatorButton.style.display = hideNavigatorButtonDisplay;
//   } else {
//     hideNavigatorButton.style.display = 'none'; // Initial state
//   }

//   showNavigator();

//   // Add event listeners to the buttons
//   showNavigatorButton.addEventListener('click', function(event) {
//     event.preventDefault();
//     showNavigator();
//   });

//   hideNavigatorButton.addEventListener('click', function(event) {
//     event.preventDefault();
//     hideNavigator();
//   });

//   // Show tooltips on page load
//   showNavigatorButton.classList.add('show-tooltip');
//   hideNavigatorButton.classList.add('show-tooltip');

//   // Hide tooltips after a few seconds
//   setTimeout(function() {
//     showNavigatorButton.classList.remove('show-tooltip');
//     hideNavigatorButton.classList.remove('show-tooltip');
//   }, 20000); // Adjust the duration as needed (5000ms = 5 seconds)
// });
