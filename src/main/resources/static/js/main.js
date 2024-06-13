document.addEventListener('DOMContentLoaded', function() {
    document.getElementById('dropdownMenuButton').addEventListener('click', function() {
        document.querySelector('.dropdown').classList.toggle('show');
    });

    window.onclick = function(event) {
        if (!event.target.matches('.dropdown-toggle')) {
            var dropdowns = document.getElementsByClassName("dropdown-content");
            for (var i = 0; i < dropdowns.length; i++) {
                var openDropdown = dropdowns[i];
                if (openDropdown.classList.contains('show')) {
                    openDropdown.classList.remove('show');
                }
            }
        }
    };
});

function changeLanguage(lang) {
    window.location.href = '?lang=' + lang;
}
