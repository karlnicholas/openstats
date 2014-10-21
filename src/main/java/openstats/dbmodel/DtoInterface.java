package openstats.dbmodel;

public interface DtoInterface<T> {
	public static enum DTOTYPE { SUMMARY, FULL };
	public T createDto(DTOTYPE dtoType);
}
