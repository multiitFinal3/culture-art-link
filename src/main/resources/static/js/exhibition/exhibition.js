let exhibitions = [];
let viewStatus = "all"; // pre, current, future, unknown 어떤 버튼 눌렀는지
let searchParams = { title: "", artist: "", museum: "" }; // 검색 조건 초기화

// 첫 실행 함수
$(document).ready(function () {
    init();


    const genreItems = document.querySelectorAll('.genre-item');

    genreItems.forEach(item => {
        item.addEventListener('click', function() {
            // 모든 genre-item에서 active 클래스 제거
            genreItems.forEach(el => {
                el.classList.remove('active');
            });

            // 클릭된 요소에 active 클래스 추가
            this.classList.add('active');
        });
    });


    // 전시 여부 버튼 할당
    $("#currentBtn").click(function () {
        changeViewStatus("current");
    });
    $("#pastBtn").click(function () {
        changeViewStatus("past");
    });
    $("#futureBtn").click(function () {
        changeViewStatus("future");
    });
    $("#unknownBtn").click(function () {
        changeViewStatus("unknown");
    });
});

// 검색 버튼 이벤트 할당, 서치버튼 누르면 서치되도록
$(document).on('click', "#searchButton", function () {
    searchExhibitions();
});


// 뷰 상태를 바꾼 다음에 렌더링
function changeViewStatus(status) {
    viewStatus = status;
    filterAndRenderExhibitions();
}

// 데이터 가져오기
async function getExhibitions() {
    const response = await axios.get(
        "/exhibition/exhibition"
    );

    // 배열 돌며 규격에 맞게 넣어줌
    exhibitions = response.data.map((item) => ({
        id: item.id,
        image: item.image,
        title: item.title,
        artist: item.artist,
        museum: item.museum,
        startDate: item.startDate ? new Date(item.startDate) : null,
        endDate: item.endDate ? new Date(item.endDate) : null,
        state: item.state,
        starsAVG:item.starsAVG
    }));

    filterAndRenderExhibitions();
}

// async function getExhibitions() {
//     const response = await axios.get("/exhibition/exhibition");
//
//     exhibitions = await Promise.all(response.data.map(async (item) => {
//         const ratingResponse = await axios.get(`/exhibition/${item.id}/rating`);
//         return {
//             id: item.id,
//             image: item.image,
//             title: item.title,
//             artist: item.artist,
//             museum: item.museum,
//             startDate: item.startDate ? new Date(item.startDate) : null,
//             endDate: item.endDate ? new Date(item.endDate) : null,
//             state: item.state,
//             averageRating: ratingResponse.data.averageRating
//         };
//     }));
//
//     filterAndRenderExhibitions();
// }

// 날짜 기준으로 날짜 필터링
function filterAndRenderExhibitions() {
    const today = new Date();
    const filteredExhibitions = exhibitions.filter((item) => {
        if (!matchesSearch(item)) return false;

        if (viewStatus === "all") return true;
        if (!item.startDate || !item.endDate)
            return viewStatus === "unknown";

        switch (viewStatus) {
            case "current":
                return item.startDate <= today && today <= item.endDate;
            case "past":
                return item.endDate < today;
            case "future":
                return today < item.startDate;
            default:
                return false;
        }
    });

    renderExhibitions(filteredExhibitions);
}

// 상세 검색(모두 소문자로 만든 후)
function matchesSearch(exhibition) {
    return (
        exhibition.title.toLowerCase().includes(searchParams.title.toLowerCase()) &&
        exhibition.artist.toLowerCase().includes(searchParams.artist.toLowerCase()) &&
        exhibition.museum.toLowerCase().includes(searchParams.museum.toLowerCase())
    );
}

// 파라미터 설정 후 그걸로 검색해줌
function searchExhibitions() {
    searchParams = {
        title: $("#searchTitle").val(),
        artist: $("#searchArtist").val(),
        museum: $("#searchMuseum").val(),
    };
    filterAndRenderExhibitions();
}

// 전시회 목록을 렌더링하는 함수
function renderExhibitions(exhibitionsToRender) {
    const $exhibitionList = $(".all-exhibitions-items");
    $exhibitionList.empty();

    $.each(exhibitionsToRender, function (index, exhibition) {
        const $exhibitionItem = $("<div>").addClass("exhibition-item");
        let interestedSrc = "https://kr.object.ncloudstorage.com/team3/common/upNo.png"
        let notInterestedSrc = "https://kr.object.ncloudstorage.com/team3/common/downNo.png"
        if(exhibition.state === "interested") {
            interestedSrc = "https://kr.object.ncloudstorage.com/team3/common/upBlue.png"
        } else if(exhibition.state === "not_interested") {
            notInterestedSrc = "https://kr.object.ncloudstorage.com/team3/common/downRed.png"
        }

        $exhibitionItem.html(`
            <div class="all-card exhibition-content" data-id="${exhibition.id}">
                <img src="${exhibition.image}" alt="${exhibition.title}">
            </div>
            <div class="exhibition-info">
                <h3 class="all-title">${exhibition.title}</h3>
                <p class="all-location">${exhibition.museum}</p>
                <p class="all-date">${exhibition.startDate ? exhibition.startDate.toISOString().split("T")[0] : "미정"} ~ ${exhibition.endDate ? exhibition.endDate.toISOString().split("T")[0] : "미정"}</p>
                <p>★ <span class="average-rating">${Number(exhibition?.starsAVG).toFixed(1)}</span></p>
            </div>
            <div class="exhibition-actions">
                <button class="exhibition-like interested-btn" data-id="${exhibition.id}">
                    <img src="${interestedSrc}" alt="">
                </button>
                <button class="exhibition-dislike interested-btn" data-id="${exhibition.id}">
                    <img src="${notInterestedSrc}" alt="">
                </button>
            </div>
        `);

        $exhibitionList.append($exhibitionItem);
    });

    // 클릭하면 상세페이지로 들어감
    $(".exhibition-content").click(function() {
        const exhibitionId = $(this).data("id");
        window.location.href = `/exhibition/detail/${exhibitionId}`;
    });

    // 찜 버튼 클릭 이벤트
    $(".exhibition-like").click(function(e) {
        e.stopPropagation();
        const exhibitionId = $(this).data("id");
        console.log("exhibition-like btn : ",exhibitionId)
        updateInterest(exhibitionId, 'interested');
    });

    // 관심없음 버튼 클릭 이벤트
    $(".exhibition-dislike").click(function(e) {
        e.stopPropagation();
        const exhibitionId = $(this).data("id");
        console.log("exhibition-dislike btn : ",exhibitionId)
        updateInterest(exhibitionId, 'not_interested');
    });
}

// 누른 전시를 찾아 관심을 업데이트 해주고 색깔 바로 변하게 설정
async function updateInterest(exhibitionId, state) {
    console.log('update interest: ',exhibitionId, state)
    try {
        const response = await axios.post('/exhibition/interested', {
            exhibitionId: Number(exhibitionId),
            state: state
        })
        let $downButton = $(`.exhibition-dislike[data-id="${exhibitionId}"] img`);
        let downSrc = $downButton.attr('src');

        let $upButton = $(`.exhibition-like[data-id="${exhibitionId}"] img`);
        let upSrc = $upButton.attr('src');
        const baseUrl = 'https://kr.object.ncloudstorage.com/team3/common/';
        if (state === 'interested') {
            if (upSrc.includes('upBlue.png')) {
                $upButton.attr('src', upSrc.replace('upBlue.png', 'upNo.png'));
            } else if (upSrc.includes('upNo.png')) {
                $upButton.attr('src', upSrc.replace('upNo.png', 'upBlue.png'));
            }
            $downButton.attr('src', baseUrl + 'downNo.png');
        }else if(state ==='not_interested') {
            if (downSrc.includes('downRed.png')) {
                $downButton.attr('src', downSrc.replace('downRed.png', 'downNo.png'));
            } else if (downSrc.includes('downNo.png')) {
                $downButton.attr('src', downSrc.replace('downNo.png', 'downRed.png'));
            }
            $upButton.attr('src', baseUrl + 'upNo.png');
        }



    }catch(e) {
        console.log('error : ',e)
        alert(`${state === 'interested'} ? "찜 설정 실패" : "관심 없음 설정 실패"`)
    }

}

//페이지에 들어갔을 때 실행 되는 init 함수
function init() {
    getExhibitions();
}