$(document).ready(function() {
    // Récupère l'URL actuelle
    const url = window.location.href;
    
    // Crée un objet URLSearchParams à partir de la partie query de l'URL
    const params = new URLSearchParams(window.location.search);
    
    // Récupère la valeur du paramètre 'id'
    const id = params.get('id');
    const menu = params.get('menu');
    console.log(id); // Affichera 'edmond' dans la console

    // Si l'ID est trouvé
    if (id) {
        // Sélectionne l'élément ayant cet ID et lui ajoute les classes 'show' et 'edmond'
        const element = $('#' + id);
        const menuLabel = $('.' + menu);

        element.addClass('show '+ id);
        menuLabel.addClass('menuactive');
        
        // Sélectionne la balise frère supérieur avec la classe '.menu-link'
        const menuLink = element.prev('.menu-link'); // Ou .siblings('.menu-link') pour tout frère

        // Si un frère avec la classe '.menu-link' est trouvé
        if (menuLink.length > 0) {
            // Retire la classe 'collapsed' et ajoute la classe 'cines'
            menuLink.removeClass('collapsed');
            menuLink.addClass('cines');
            
            // Ajoute aria-expanded="true" à cet élément
            menuLink.attr('aria-expanded', 'true');
        }
    }
});