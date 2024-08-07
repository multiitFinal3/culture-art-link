let boardId;

document.addEventListener('DOMContentLoaded', async function() {
    const pathSegments = window.location.pathname.split('/');
    boardId = pathSegments[pathSegments.length - 1];

    if (boardId && !isNaN(boardId)) {
        await loadBoardData();
    } else {
        console.error('유효한 Board ID를 찾을 수 없습니다.');
    }

    const imageUpload = document.getElementById('imageUpload');
    const editor = document.getElementById('editor');

    imageUpload.addEventListener('change', handleImageUpload);
    document.getElementById('updateForm').addEventListener('submit', handleFormSubmit);
});

async function loadBoardData() {
    try {
        const response = await axios.get(`/board/${boardId}`);
        const board = response.data;
        document.getElementById('title').value = board.title;
        document.getElementById('category').value = board.genre;
        document.getElementById('editor').innerHTML = board.content;
    } catch (error) {
        console.error('게시글 데이터를 불러오는 중 오류 발생:', error);
        alert('게시글 데이터를 불러오는데 실패했습니다.');
    }
}

async function handleImageUpload(e) {
    const file = e.target.files[0];
    if (file) {
        try {
            const formData = new FormData();
            const filePath = `/board/${new Date().getTime()}.png`
            formData.append('file', file);
            formData.append('path', filePath);

            await axios.post('/file/upload', formData, {
                headers: {
                    'Content-Type': 'multipart/form-data'
                }
            });

            const img = document.createElement('img');
            img.src = "https://kr.object.ncloudstorage.com/team3" + filePath;
            img.style.maxWidth = '100%';
            editor.appendChild(img);
        } catch (error) {
            console.error('Error uploading image:', error);
            alert('이미지 업로드에 실패했습니다.');
        }
    }
}

async function handleFormSubmit(e) {
    e.preventDefault();
    const title = document.getElementById('title').value;
    const content = editor.innerHTML;
    const category = document.getElementById('category').value;

    try {
        await axios.patch('/board', {
            id: boardId,
            title,
            content,
            genre: category
        });
        alert('글이 수정되었습니다.');
        window.location.href = `/board/view/${boardId}`;
    } catch (error) {
        console.error('글 수정 중 오류 발생:', error);
        alert('글 수정에 실패했습니다.');
    }
}