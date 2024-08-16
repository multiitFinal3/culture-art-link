let boardId;

document.addEventListener('DOMContentLoaded', async function () {
    const pathSegments = window.location.pathname.split('/');
    boardId = pathSegments[pathSegments.length - 1];

    console.log('boardId  :', boardId)

    if (boardId) {
        await loadPostDetails();
        await loadComments();
    }

    document.getElementById('editBtn').addEventListener('click', editPost);
    document.getElementById('deleteBtn').addEventListener('click', deletePost);
    document.getElementById('commentForm').addEventListener('submit', submitComment);
});

async function loadPostDetails() {
    try {
        const response = await axios.get(`/board/${boardId}`);
        const post = response.data;

        console.log('post : ', post)

        if (!post.auth) {
            $(".button-group").hide();
        }
        document.getElementById('postTitle').textContent = post.title;
        document.getElementById('postContent').innerHTML = post.content;
    } catch (error) {
        console.error('Error loading post details:', error);
    }
}

async function loadComments() {
    try {
        const response = await axios.get(`/board/${boardId}/comment`);
        const comments = response.data;
        const commentsList = document.getElementById('commentsList');
        commentsList.innerHTML = '';
        comments.forEach(comment => {
            const commentDiv = document.createElement('div');
            commentDiv.className = 'comment';
            commentDiv.innerHTML = `
                <p class="comment-content">${comment.author}</p>
                <p>${comment.content}</p>
                <button class="delete-comment-btn btn btn-outline-secondary" data-comment-id="${comment.id}">삭제</button>
            `;
            commentsList.appendChild(commentDiv);

            const deleteBtn = commentDiv.querySelector('.delete-comment-btn');
            if (comment.auth) {
                deleteBtn.style.display = 'block';
                deleteBtn.addEventListener('click', () => deleteComment(comment.id));
            }
        });
    } catch (error) {
        console.error('댓글을 불러오는 중 오류 발생:', error);
    }
}

async function deleteComment(commentId) {
    if (confirm('정말로 이 댓글을 삭제하시겠습니까?')) {
        try {
            await axios.delete(`/board/${boardId}/comment`, {data: {id: commentId}});
            await loadComments(); // 댓글 목록 새로고침
            alert('댓글이 삭제되었습니다.');
        } catch (error) {
            console.error('댓글 삭제 중 오류 발생:', error);
            alert('댓글 삭제에 실패했습니다.');
        }
    }
}

async function editPost() {
    window.location.href = `/board/view/update/${boardId}`;
}

async function deletePost() {
    if (confirm("정말로 이 글을 삭제하시겠습니까?")) {
        try {
            await axios.delete('/board', {data: {id: boardId}});
            alert("글이 삭제되었습니다.");
            window.location.href = '/board/view'; // 게시판 목록으로 리다이렉트
        } catch (error) {
            console.error('Error deleting post:', error);
        }
    }
}

async function submitComment(event) {
    event.preventDefault();
    const commentContent = document.getElementById('newComment').value;
    try {
        await axios.post(`/board/${boardId}/comment`, {content: commentContent});
        document.getElementById('newComment').value = '';
        await loadComments();
    } catch (error) {
        console.error('Error submitting comment:', error);
    }
}