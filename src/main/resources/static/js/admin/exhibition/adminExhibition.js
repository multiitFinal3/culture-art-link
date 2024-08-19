// 유틸리티 함수
const utils = {
    parseDateRange(dateString) {
        if (!dateString) return {startDate: null, endDate: null};

        const splitStr = dateString.includes(" - ") ? " - " : "~";
        const [start, end] = dateString.trim().split(splitStr);

        function formatDate(date) {
            if (!date) return null;
            if (date.includes("-") && date.length > 10) return date.substring(0, 10);
            if (date.length === 8)
                return date.replace(/(\d{4})(\d{2})(\d{2})/, "$1-$2-$3");
            if (date.includes("-") && date.length === 10) return date;
            return date.slice(0, 10);
        }

        return {
            startDate: formatDate(start?.trim()),
            endDate: formatDate(end?.trim()),
        };
    },

    // 페이징 버튼 만드는 함수
    createPageButton(text, onClick) {
        const button = document.createElement("button");
        button.innerText = text;
        button.addEventListener("click", onClick);
        button.className = "pageBtn1"
        return button;
    },
};

// 전역 변수
let exhibition = [];
let exhibitionDB = [];
let currentPage = 1;
const itemsPerPage = 10;
const maxPageButtons = 10;

// 데이터 관리 함수
async function manageExhibitions(action, data) {
    try {
        let response;
        switch (action) {
            case "save":
                response = await axios.post(
                    "/admin/exhibition-regulate/exhibition",
                    data
                );
                console.log("save : ", data);
                alert(`${data.length} 데이터가 저장되었습니다.`);
                break;
            case "delete":
                response = await axios.delete(
                    "/admin/exhibition-regulate/db-exhibition",
                    {data: data.map((v) => v.id)}
                );
                alert("삭제완료");
                break;
            case "update":
                response = await axios.patch(
                    "/admin/exhibition-regulate/db-exhibition",
                    data
                );
                alert("수정 완료");
                break;
        }
        // 액션 후 데이터 다시 가져오기
        await fetchData();
    } catch (error) {
        console.error(`Error ${action}ing exhibition:`, error);
        alert(`${action} 중 오류가 발생했습니다.`);
    }
}

// 페이지에 따른 데이터 렌더링 함수
function renderData(allData, targetId, isPagination = false) {
    const startIndex = (currentPage - 1) * itemsPerPage;
    const endIndex = startIndex + itemsPerPage;
    const pageData = allData?.slice(startIndex, endIndex);

    console.log('render data ==== allData : ', allData)

    // true: 테이블 데이터 렌더링, false: 페이징 버튼 렌더링
    if (!isPagination) {
        const tableBody = document.getElementById(targetId);
        tableBody.innerHTML = "";

        // API 리스트 테이블에만 정렬 적용 (db에 있는 데이터는 뒤로 넘기기)
        const dataToRender =
            targetId === "list2" ? sortDataByDBExistence(allData) : allData;

        const startIndex = (currentPage - 1) * itemsPerPage;
        const endIndex = startIndex + itemsPerPage;

        console.log('render data ==== dataToRender : ', dataToRender)
        const pageData = dataToRender?.slice(startIndex, endIndex);

        // 테이블 형식에 맞춘 데이터 렌더링
        pageData.forEach((item, index) => {
            const {startDate, endDate} = utils.parseDateRange(item.PERIOD);
            item.startDate =
                (item.startDate || startDate || "").substring(0, 10) || "미정";
            item.endDate = (item.endDate || endDate || "").substring(0, 10) || "미정";

            item.start_date = item.startDate;
            item.end_date = item.endDate;

            const row = createTableRow(
                item,
                startIndex + index,
                targetId === "list2"
            );
            tableBody.innerHTML += row;
        });

        if (targetId === "list1") {
            addEditButtonListeners();
        }
    } else {
        const totalPages = Math.ceil(allData.length / itemsPerPage);
        const paginationElement = document.getElementById(targetId);
        paginationElement.innerHTML = "";

        // 페이징 계산
        const pageGroup = Math.ceil(currentPage / maxPageButtons);
        let startPage = (pageGroup - 1) * maxPageButtons + 1;
        let endPage = Math.min(pageGroup * maxPageButtons, totalPages);


        appendPageButton(paginationElement, "처음", () =>
            goToPage(1, allData, targetId)
        );
        if (currentPage > 1) {
            appendPageButton(paginationElement, "이전", () =>
                goToPage(Math.max(startPage - maxPageButtons, 1), allData, targetId)
            );
        }

        for (let i = startPage; i <= endPage; i++) {
            appendPageButton(
                paginationElement,
                i,
                () => goToPage(i, allData, targetId),
                i === currentPage
            );
        }

        if (currentPage < totalPages) {
            appendPageButton(paginationElement, "다음", () =>
                goToPage(Math.min(startPage + maxPageButtons, totalPages), allData, targetId)
            );
        }
        appendPageButton(paginationElement, "마지막", () =>
            goToPage(totalPages, allData, targetId)
        );
    }
}

// 테이블 렌더링할 때 불러오는 함수
function createTableRow(item, index, isApiList) {
    // db 테이블인지, api 테이블인지 확인
    if (isApiList) {
        const existsInDB = exhibitionDB.some(
            (dbItem) => dbItem.localId === item.LOCAL_ID
        );
        return `
      <tr>
        <td scope="col" class="td-index">${
            existsInDB
                ? "DB 존재"
                : `<input type="checkbox" class="row-checkbox" data-index="${index}">`
        }</td>
        <td scope="col" class="td-number">${index + 1}</td>
        <td scope="col" title="${item.TITLE}">${item.TITLE}</td>
        <td scope="col" title="${item.CNTC_INSTT_NM}">${item.CNTC_INSTT_NM}</td>
        <td scope="col" title="${item.startDate}">${item.startDate}</td>
        <td scope="col" title="${item.endDate}">${item.endDate}</td>
      </tr>
    `;
    } else {
        return `
      <tr>
        <td scope="col" class="td-index"><input type="checkbox" class="row-checkbox" data-index="${index}"></td>
        <td scope="col" class="td-number">${index + 1}</td>
        <td scope="col" class="td-title" title="${item.title}">${
            item.title
        }</td>
        <td scope="col" class="td-artist" title="${item.artist}">${
            item.artist
        }</td>
        <td scope="col" title="${item.museum}">${item.museum}</td>
        <td scope="col" title="${item.startDate}">${item.startDate}</td>
        <td scope="col" title="${item.endDate}">${item.endDate}</td>
        <td scope="col" class="td-price" title="${item.price}">${
            item.price
        }</td>
        <td scope="col"><img class="resize-img" src="${
            item.image
        }" style="width: 150px;"></img></td>
        <td scope="col"><button class="edit-button btn btn-primary" >수정</button></td>
      </tr>
    `;
    }
}

// 페이징 버튼 넣어줄때 사용하는 함수
function appendPageButton(container, text, onClick, isActive = false) {
    const button = utils.createPageButton(text, onClick);
    if (isActive) button.classList.add("active");
    container.appendChild(button);
}

// 페이징 버튼 눌렀을 때 어떤 데이터를 불러오는지 처리
function goToPage(page, allData, targetId) {
    currentPage = page;
    renderData(allData, targetId.replace("pageNum", "list"));
    renderData(allData, targetId, true);
}

async function initBtn(type) {
    if (type === 'DB') {
        $("#searchTitleDB").val('')
        $("#searchMuseumDB").val('')
        $("#searchArtistDB").val('')
        search('DB')
    } else {
        $("#searchTitle").val('')
        $("#searchMuseum").val('')
        search('API')
    }
}

// 검색 함수, db 데이터 검색인지 api 데이터 검색인지 확인하고 검색조건의 데이터를 가져와 검색
async function search(type) {
    const title = $(`#searchTitle${type === "DB" ? "DB" : ""}`).val();
    const artist = type === "DB" ? $("#searchArtistDB").val() : null;
    const museum = $(`#searchMuseum${type === "DB" ? "DB" : ""}`).val();

    let url =
        type === "DB"
            ? "/exhibition/search-exhibition"
            : "/admin/exhibition-regulate/exhibition";
    let params = new URLSearchParams();

    if (title) params.append("title", title);
    if (artist) params.append("artist", artist);
    if (museum) params.append("museum", museum);

    try {
        const response = await axios.get(`${url}?${params.toString()}`);
        let data = response.data;

        if (type !== "DB") {
            data = data.filter(
                (v) =>
                    (!title || v.TITLE.includes(title)) &&
                    (!museum || v.CNTC_INSTT_NM.includes(museum))
            );
        }

        // 검색 결과 저장 및 currentPage 초기화
        if (type === "DB") {
            exhibitionDB = data;
        } else {
            exhibition = data;
        }
        // 검색한 후 제일 앞 페이지로 보내줌
        currentPage = 1;

        const targetList = `list${type === "DB" ? "1" : "2"}`;
        const targetPagination = `pageNum${type === "DB" ? "1" : "2"}`;

        // 가져온 데이터대로 화면 렌더링
        renderData(data, targetList);
        renderData(data, targetPagination, true);
    } catch (error) {
        console.error("검색 중 오류 발생:", error);
    }
}

// 데이터 가져오기
async function fetchData() {
    const fetchExhibition = axios.get("/admin/exhibition-regulate/exhibition")
        .then(response => {
            exhibition = response.data;
            renderData(exhibition, "list2");
            renderData(exhibition, "pageNum2", true);
        })
        .catch(error => console.error("전시회 데이터 가져오기 실패:", error));

    const fetchExhibitionDB = axios.get("/admin/exhibition-regulate/db-exhibition")
        .then(response => {
            exhibitionDB = response.data;
            renderData(exhibitionDB, "list1");
            renderData(exhibitionDB, "pageNum1", true);
        })
        .catch(error => console.error("DB 전시회 데이터 가져오기 실패:", error));

    await Promise.allSettled([fetchExhibition, fetchExhibitionDB]);

}

// 이벤트 리스너 등록 함수, 버튼 바인딩, 동적으로 할당하기 위한 문법 사용
// 외부 요인(ex: js)으로 html이 추가되면 인식하지 못해 동적 할당
function registerEventListeners() {
    document.getElementById("apiInsertBtn").addEventListener("click", () => {
        const checkedData = getCheckedData("list2");

        console.log('save checkedData : ', checkedData)
        manageExhibitions("save", checkedData);
    });

    document.getElementById("dbDeleteBtn").addEventListener("click", () => {
        const checkedData = getCheckedData("list1");
        manageExhibitions("delete", checkedData);
    });

    document.getElementById("dbUpdateBtn").addEventListener("click", () => {
        const checkedData = getCheckedData("list1").map(
            v =>
                ({
                    ...v,
                    startDate: (v.startDate === '미정' ? null : v.startDate),
                    endDate: (v.endDate === '미정' ? null : v.endDate)
                })
        );

        console.log('update checked data : ', checkedData)


        manageExhibitions("update", checkedData);
    });

    document
        .getElementById("searchButtonDB")
        .addEventListener("click", () => search("DB"));
    document
        .getElementById("searchButton")
        .addEventListener("click", () => search("API"));


    document
        .getElementById("initSearchButtonDB")
        .addEventListener("click", () => initBtn("DB"));
    document
        .getElementById("initSearchButton")
        .addEventListener("click", () => initBtn("API"));
}

// 체크된 데이터 목록 가져오기
function getCheckedData(listId) {
    const checkedBoxes = document.querySelectorAll(
        `#${listId} .row-checkbox:checked`
    );
    return Array.from(checkedBoxes).map((checkbox) => {
        const index = parseInt(checkbox.getAttribute("data-index"));
        // 위의 테이블인지, 아래 테이블인지 체크하여 어떤 리스트를 가져올것인지 선택
        return listId === "list2" ? exhibition[index] : exhibitionDB[index];
    });
}

// 수정 버튼 할당
function addEditButtonListeners() {
    const editButtons = document.querySelectorAll("#list1 .edit-button");
    editButtons.forEach((button) => {
        button.addEventListener("click", function () {
            const row = this.closest("tr");
            // 수정버튼 클릭 시 수정 형식 출력
            makeRowEditable(row);
        });
    });
}

// 존재 여부에 따라 정렬
function sortDataByDBExistence(data) {
    return data.sort((a, b) => {
        const aExistsInDB = exhibitionDB.some(
            (item) => item.localId === a.LOCAL_ID
        );
        const bExistsInDB = exhibitionDB.some(
            (item) => item.localId === b.LOCAL_ID
        );
        if (aExistsInDB && !bExistsInDB) return 1;
        if (!aExistsInDB && bExistsInDB) return -1;
        return 0;
    });
}

// 수정버튼 클릭 시 수정 형식 출력하는 함수
function makeRowEditable(row) {
    const cells = row.querySelectorAll("td:not(:first-child):not(:last-child)");
    cells.forEach((cell, index) => {
        if (index !== 0) {
            // 번호 열은 수정 불가
            const originalContent = cell.textContent;
            cell.innerHTML = `<input class="width-100" type="text" value="${originalContent}">`;
        }
    });

    const actionsCell = row.querySelector("td:last-child");
    actionsCell.innerHTML = `
    <button onclick="saveRowChanges(this)" class="btn btn-primary">저장</button>
    <button onclick="cancelRowEdit(this)" class="btn btn-primary">취소</button>
  `;
}

// 수정 후 수정 형식 지워주고 수정한 데이터로 덮어쓰기
function saveRowChanges(button) {
    const row = button.closest("tr");
    const inputs = row.querySelectorAll('input[type="text"]');
    const updatedData = {};
    const fields = ["title", "artist", "museum", "startDate", "endDate", "price"];

    inputs.forEach((input, index) => {
        updatedData[fields[index]] = input.value;
    });

    const rowIndex = parseInt(
        row.querySelector(".row-checkbox").getAttribute("data-index")
    );
    exhibitionDB[rowIndex] = {...exhibitionDB[rowIndex], ...updatedData};

    renderData(exhibitionDB, "list1");
}

// 수정 취소
function cancelRowEdit(button) {
    renderData(exhibitionDB, "list1");
}

// 초기화 함수(처음 시작했을 때 실행되는 함수 모음)
async function init() {
    await fetchData();
    registerEventListeners();
}

// 문서 로드 완료 시 초기화
$(document).ready(init);
