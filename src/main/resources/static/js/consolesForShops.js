const select = document.getElementById("1");
const ps4 = document.getElementById("a-option");
const ps4Label = document.querySelector("label[for=a-option]")
const ps3 = document.getElementById("b-option");
const ps3Label = document.querySelector("label[for=b-option]");
const xbox360 = document.getElementById("c-option");
const xbox360Label = document.querySelector("label[for=c-option]")
const xboxOne = document.getElementById("d-option");
const xboxOneLabel = document.querySelector("label[for=d-option]")
const nintendo = document.getElementById("e-option");
const nintendoLabel = document.querySelector("label[for=e-option]")

select.addEventListener("click", function (event) {
    event.preventDefault();
    if (select.value === 'ShopGracz') {
        ps3.style.display = 'none';
        ps3Label.style.display = 'none';
        xbox360.style.display = 'none';
        xbox360Label.style.display = 'none';
        xboxOne.style.display = 'none';
        xboxOneLabel.style.display = 'none';

    } else {
        ps3.style.display = '';
        ps3Label.style.display = '';
        xbox360.style.display = '';
        xbox360Label.style.display = '';
        xboxOne.style.display = '';
        xboxOneLabel.style.display = '';
    }

})