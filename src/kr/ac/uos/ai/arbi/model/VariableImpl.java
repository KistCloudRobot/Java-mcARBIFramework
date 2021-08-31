package kr.ac.uos.ai.arbi.model;


class VariableImpl implements Variable {
	private final String				_name;
	
	VariableImpl(String name) {
		_name = name;
	}
	
	public String getName() {
		return _name;
	}
	
	public String toString() {
		return _name;
	}
	
}
