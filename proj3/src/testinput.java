// the program
// declList
// varDecl
int githubCode;
// structDecl
struct student {
    bool isBoy;
    int grade;
    int studentId;
    int code;
}

// void fnDecl
        // empty formals
void main() {
    // fnBody
    // varDeclList
    //int decl
    int i;
    int n;
    // stmtList;
    // assign stmt, assignExp
    i = 0;
    // read stmt,
    cin >> n;
    cin >> githubCode;
    // test while stmt
        // less exp
    while (i < n) {
        // varDeclList
        int grade;
        // bool varDecl
        bool cheat;
        // struct varDecl
        struct student st;
        // stmtList
        cin >> st.grade;
        // fncall
                      // actualList
        cheat = isCheated(st.code, githubCode);
        grade = getGrade(st.grade, cheat);
        // test if-else stmt
            // exp is a fnCall
        if (isA(grade, st.isBoy)) {
            printA();
        } else {
            printB();
        }
        // postInc stmt
        i++;
    }
    // return stmt without exp;
    return;
}


// int fnDecl
           // formals
int getGrade(int midterm, int project, int final, bool isCheated) {
    // fnBody
    // varDeclList
    int grade;
    // stmtList
    // if stmt
    if (isCheated) {
        // return stmt with a long esp
        // unary minus, + ,- , *, / expressions: test precedence and associativity
        return -1 + 5 - 2 * (6 / (3 - 0)) + 4 - 4;
    }
    // assign stmt with a long exp
    grade = (midterm * 2 + project * 5 + final * 3) / 10;
    // return stmt with a id
    return grade;
}

// bool fnDecl
        // formals
bool isA(bool isBoy, int grade) {
    // test !, ||, and >=
    if (!isBoy || grade >= 85) {
        return true;
    }
    // test &&, <
    if (isBoy && grade < 85) {
        return false;
    }
}

// bool fnDecl
        // formals
bool isCheated(int yourCode, int githubCode) {
    int projectNum;
    int a;
    int b;
    cin >> a;
    cin >> b
    // test repeat stmt
    repeat (projectNum > 0) {
        // ||, &&, !, !=, = : test precedence and associativity
                                                        // test assignExp in if stmt
        if (yourCode / githubCode > 0.9 || a != b && -a <= 0 && !(a = 1)) {
            return true;
        }
        // postDec stmt
        projectNum--;
    }
    return false;
}

void printA() {
    // test write stmt
    cout << "This is an A student";
}

void printB() {
    cout << "This is not an A student";
}
