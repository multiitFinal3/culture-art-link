document.addEventListener("DOMContentLoaded", function() {
        const stars = document.querySelectorAll(".star");
        stars.forEach(star => {
            star.addEventListener("click", function() {
                const ratingValue = this.getAttribute("data-value");

                // 선택된 별 이하의 별들을 노란색으로 변경
                stars.forEach(s => {
                    s.classList.remove("selected");
                    if (s.getAttribute("data-value") <= ratingValue) {
                        s.classList.add("selected");
                    }
                });
            });
        });
    });