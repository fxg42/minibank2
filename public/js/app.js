// When the page is fully loaded,
$(function () {

// find the dropdown button and the dropdown menu.
  var btn = $('#dropdown-btn');
  var menu = $('.dropdown-menu');

// Define a template the renders a menu item from a person.
  var menuItemTemplate = function (person) {
    return "<li><a href='/person/show?id="+ person.ID +"'>" + person.NAME + "</a></li>";
  };

// When the user clicks on the dropdown button,
  btn.on('click', function () {
// query the server for the person list in JSON format.
    $.ajax({
      url: '/person/list.json',
      dataType: 'json',
      success: function (people) {
// When the server responds,
// clear the dropdown menu,
        menu.empty();
// and for each person in the list returned,
        for(var i=0, len=people.length; i<len; i++) {
// append a menu item to the dropdown menu.
          menu.append(menuItemTemplate(people[i]));
        }
      }
    });
  });
});
